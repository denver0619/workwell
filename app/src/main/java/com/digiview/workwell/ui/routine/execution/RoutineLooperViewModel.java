package com.digiview.workwell.ui.routine.execution;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.digiview.workwell.data.models.RoutineExercise;
import com.digiview.workwell.data.models.RoutineExerciseDetailDTO;
import com.digiview.workwell.services.exercises.AbstractExercise;
import com.digiview.workwell.services.exercises.BaseExerciseDynamic;
import com.digiview.workwell.services.exercises.ExerciseFactory;

import java.util.List;
import java.util.concurrent.CountDownLatch;

public class RoutineLooperViewModel extends ViewModel {

    private final MutableLiveData<Boolean> isRoutineFinished = new MutableLiveData<>(false);
    public LiveData<Boolean> getIsRoutineFinished() {
        return isRoutineFinished;
    }
    public void setIsRoutineFinished(Boolean isRoutineFinished) {
        this.isRoutineFinished.setValue(isRoutineFinished);
    }

    // for ui update==================================================



    // FOR ROUTINE EXECUTION====================================
    private List<RoutineExerciseDetailDTO> routine;
    private final MutableLiveData<BaseExerciseDynamic> currentExercise = new MutableLiveData<>();

    public void setRoutine(List<RoutineExerciseDetailDTO> routine) {
        this.routine = routine;
    }

    public LiveData<BaseExerciseDynamic> getCurrentExercise() {
        return currentExercise;
    }


    // TRANSITION STATUS
    private final MutableLiveData<RoutineConstants.TRANSITION_STATE> transitionState = new MutableLiveData<>();
    private LiveData<RoutineConstants.TRANSITION_STATE> getTransistionState() {
        return transitionState;
    }
    public void setTransitionState(RoutineConstants.TRANSITION_STATE transitionState) {
        this.transitionState.setValue(transitionState);
        this.transitionState.setValue(RoutineConstants.TRANSITION_STATE.NONE);
    }

    // EXECUTION STATUS
    private final MutableLiveData<RoutineConstants.EXECUTION_STATE> executionState = new MutableLiveData<>();
    private LiveData<RoutineConstants.EXECUTION_STATE> getExecutionState() {
        return executionState;
    }

    public void setExecutionState(RoutineConstants.EXECUTION_STATE executionState) {
        this.executionState.setValue(executionState);
        this.executionState.setValue(RoutineConstants.EXECUTION_STATE.NONE);
    }

    private final MutableLiveData<RoutineConstants.REMINDER_STATE> reminderState = new MutableLiveData<>();
    private LiveData<RoutineConstants.REMINDER_STATE> getReminderState() {
        return  reminderState;
    }

    public void setReminderState(RoutineConstants.REMINDER_STATE reminderState) {
        this.reminderState.setValue(reminderState);
        this.reminderState.setValue(RoutineConstants.REMINDER_STATE.NONE);
    }

//    public void executeRoutine() {
//        ExerciseFactory exerciseFactory = new ExerciseFactory();
//        Thread thread = new Thread(() -> {
//            int counter = 0;
//            for (RoutineExercise exerciseEntity : routine) {
//                // Update the UI with the current counter
//                counter++;
//                toastMsg.postValue(String.valueOf(counter));
//
//                // Create and post the current exercise
//                currentExercise.postValue(exerciseFactory.createExercise(
//                        exerciseEntity.getExerciseName(),
//                        exerciseEntity.getReps(),
//                        exerciseEntity.getDuration()
//                ));
//
//                // Transition to ExerciseTransitionFragment
//                postFragmentTransition(ExerciseTransitionFragment.class, exerciseEntity.getExerciseName());
//
//                // Use CountDownLatch to wait for the transition and exercise to finish
//                CountDownLatch transitionLatch = new CountDownLatch(1);
//                CountDownLatch executionLatch = new CountDownLatch(1);
//
//                // Observer for transition state
//                observeTransitionState(transitionLatch);
//
//                // Wait for the transition to finish
//                awaitLatch(transitionLatch, "Waiting for transition to finish..."+counter);
//
//                // Post to change the fragment to RoutineExecutionFragment
//                postFragmentTransition(RoutineExecutionFragment.class);
//
//                // Observer for execution state
//                observeExecutionState(executionLatch);
//                // Wait for the exercise to finish
//                awaitLatch(executionLatch, "Waiting for exercise to finish..."+counter);
//                try {
//                    Thread.sleep(3000);
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//
//            postFragmentTransition(VideoConvertFragment.class);
//        });
//
//        thread.start();
//    }

    //

    private Thread thread;
    public void executeRoutine() {
        ExerciseFactory exerciseFactory = new ExerciseFactory();
        thread = new Thread(() -> {
            int counter = 0;

            postFragmentTransition(ReminderFragment.class);

            CountDownLatch reminderLatch = new CountDownLatch(1);
            observeReminderState(reminderLatch);
            awaitLatch(reminderLatch, "Wating for user to confirm reminders....");

            for (RoutineExerciseDetailDTO exerciseEntity : routine) {
                // Update the UI with the current counter
                counter++;
                toastMsg.postValue(String.valueOf(counter));

                // Create and post the current exercise
//                currentExercise.postValue(exerciseFactory.createExercise(
//                        exerciseEntity.getExerciseName(),
//                        exerciseEntity.getReps(),
//                        exerciseEntity.getDuration()
//                ));

                currentExercise.postValue(
                        new BaseExerciseDynamic(exerciseEntity)
                );

                // Transition to ExerciseTransitionFragment
                postFragmentTransition(ExerciseTransitionFragment.class, exerciseEntity.getExerciseName());

                // Use CountDownLatch to wait for the transition and exercise to finish
                CountDownLatch transitionLatch = new CountDownLatch(1);
                CountDownLatch executionLatch = new CountDownLatch(1);

                // Observer for transition state
                observeTransitionState(transitionLatch);

                // Wait for the transition to finish
                awaitLatch(transitionLatch, "Waiting for transition to finish..."+counter);

                // Post to change the fragment to RoutineExecutionFragment
                postFragmentTransition(RoutineExecutionFragment.class);

                // Observer for execution state
                observeExecutionState(executionLatch);
                // Wait for the exercise to finish
                awaitLatch(executionLatch, "Waiting for exercise to finish..."+counter);
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            postFragmentTransition(VideoConvertFragment.class);
        });

        thread.start();
    }

    private void postFragmentTransition(Class<? extends  Fragment> fragmentClass) {
        new Handler(Looper.getMainLooper()).post(() -> {
            try {
                destination.setValue(fragmentClass.newInstance());
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void postFragmentTransition(Class<? extends  Fragment> fragmentClass, String exerciseName) {
        new Handler(Looper.getMainLooper()).post(() -> {
            try {
                Fragment fragment = fragmentClass.newInstance();
                Bundle args = new Bundle();
                args.putString("exerciseName", exerciseName);
                fragment.setArguments(args);
                destination.setValue(fragment);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void observeTransitionState(CountDownLatch latch) {
        new Handler(Looper.getMainLooper()).post(() -> {
            getTransistionState().observeForever(new Observer<RoutineConstants.TRANSITION_STATE>() {
                @Override
                public void onChanged(RoutineConstants.TRANSITION_STATE transitionState) {
                    if (transitionState.equals(RoutineConstants.TRANSITION_STATE.FINISHED)) {
                        getTransistionState().removeObserver(this);
                        latch.countDown();
                    }
                }
            });
        });
    }

    private void observeExecutionState(CountDownLatch latch) {
        new Handler(Looper.getMainLooper()).post(() -> {
            getExecutionState().observeForever(new Observer<RoutineConstants.EXECUTION_STATE>() {
                @Override
                public void onChanged(RoutineConstants.EXECUTION_STATE executionState) {
                    if (executionState.equals(RoutineConstants.EXECUTION_STATE.FINISHED)) {
                        getExecutionState().removeObserver(this);
                        latch.countDown();
                    }
                }
            });
        });
    }

    private void observeReminderState(CountDownLatch latch) {
        new Handler(Looper.getMainLooper()).post(() -> {
            getReminderState().observeForever(new Observer<RoutineConstants.REMINDER_STATE>() {
                @Override
                public void onChanged(RoutineConstants.REMINDER_STATE reminderState) {
                    if (reminderState.equals(RoutineConstants.REMINDER_STATE.ACCEPTED)) {
                        getReminderState().removeObserver(this);
                        latch.countDown();
                    }
                }
            });
        });
    }

    private void awaitLatch(CountDownLatch latch, String logMessage) {
        try {
            Log.d("Latch", logMessage);
            latch.await();  // Block until latch is counted down
            Log.d("Latch", "Latch is decremented, moving forward.");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // TOAST
    private final MutableLiveData<String> toastMsg = new MutableLiveData<>();
    public LiveData<String> getToastMsg() {
        return toastMsg;
    }



    // FOR NAVIGATING FRAGMENTS

    private MutableLiveData<Fragment> destination = new MutableLiveData<>();
    public LiveData<Fragment> getDestination() {
        return destination;
    }

    public void setDestination( Fragment destination) {
        this.destination.setValue(destination);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if(thread != null) {
            thread.interrupt();
        }
    }
}
