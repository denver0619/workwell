package com.digiview.workwell.ui.routine.execution;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.digiview.workwell.services.TTSHelper;
import com.digiview.workwell.services.exercises.Exercise;
import com.google.mediapipe.tasks.vision.poselandmarker.PoseLandmarkerResult;

import java.io.IOException;
import java.util.Objects;

public class RoutineExecutionViewModel extends ViewModel {// TODO: Implement the ViewModel
    private MediaPlayer mediaPlayer;
    public void setMediaPlayer(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
    }
    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    private Context context;
    public void setContext(Context context) {
        this.context = context;
    }

    private final MutableLiveData<Exercise> exercise = new MutableLiveData<>();
    public void setExercise(Exercise exercise) {
        this.exercise.setValue(exercise);
    }

    private final MutableLiveData<Exercise.ExerciseResult> exerciseResult = new MutableLiveData<>();
    public void setExerciseResult(Exercise.ExerciseResult exerciseResult) {
        this.exerciseResult.setValue(exerciseResult);
    }

    private final MutableLiveData<PoseLandmarkerResult> poseLandmarkerResult = new MutableLiveData<>();
    public void setPoseLandmarkerResult(PoseLandmarkerResult poseLandmarkerResult) {
        this.poseLandmarkerResult.setValue(poseLandmarkerResult);
    }
    public LiveData<PoseLandmarkerResult> getPoseLandmarkerResult() {
        return poseLandmarkerResult;
    }

    private final MutableLiveData<RoutineConstants.EXECUTION_STATE> executionState = new MutableLiveData<>();
    public void setExecutionState(RoutineConstants.EXECUTION_STATE executionState) {
        this.executionState.setValue(executionState);
    }
    public final LiveData<RoutineConstants.EXECUTION_STATE> getExecutionState() {
        return this.executionState;
    }



    // AUDIO FEEDBACK

    private final MutableLiveData<TTSHelper> ttsHelper = new MutableLiveData<>();

    public void setTtsHelper(Context context) {
        ttsHelper.setValue(new TTSHelper(context));
    }
    public LiveData<TTSHelper> getTtsHelper() {
        return ttsHelper;
    }


    // processing

    private final MutableLiveData<String> statusText = new MutableLiveData<>();
    private final MutableLiveData<Integer> statusColor = new MutableLiveData<>();
    private final MutableLiveData<Double> angle2d = new MutableLiveData<>();
    private final MutableLiveData<Double> angle3d = new MutableLiveData<>();
    private final MutableLiveData<double[]> angles = new MutableLiveData<>();
    private final MutableLiveData<Integer> counter = new MutableLiveData<>(0);
    private final MutableLiveData<Long> timeLeft = new MutableLiveData<>();

    private int relaxedCount = 0;
    private int stretchedCount = 0;
    private int stateThreshold = 3; // Example threshold, define as needed
    private Exercise.STATUS lastStatus;
    private boolean isFromRelaxed = false;
    public LiveData<String> getStatusText() { return statusText; }
    public LiveData<Integer> getStatusColor() { return statusColor; }
    public LiveData<Double> getAngle2d() { return angle2d; }
    public LiveData<Double> getAngle3d() { return angle3d; }
    public LiveData<double[]> getAngles() {return angles; }
    public LiveData<Integer> getCounter() { return counter; }
    public LiveData<Long> getTimeLeft() {return timeLeft; }


    /*TODO: Implement PoseSmoother
    *  Pass the landmarks to it
    *  Pass the result to exercise
    */
    public PoseSmoother poseSmoother = new PoseSmoother(25);

    public void processLandmarkerResult(PoseLandmarkerResult landmarkerResults) {
        setExecutionState(RoutineConstants.EXECUTION_STATE.PREPARING);
        if (exercise.getValue() == null) {
            return;
        }

        if (landmarkerResults != null && !landmarkerResults.landmarks().isEmpty()) {
            //pass the detected keypoints for calculation
            //TODO: Implementing PoseSmoother
//            exercise.getValue().setLandmarkerResult(landmarkerResults.landmarks().get(0));
            exercise.getValue().setLandmarkerResult(
                    poseSmoother.update(landmarkerResults.landmarks().get(0))
            );

            //get the result from calculation
            Exercise.ExerciseResult result = exercise.getValue().excerciseResult();
            Exercise.STATUS position = result.getPosition();
            lastStatus = result.getLastPosition();
            angles.setValue(result.getAngles());

            angle2d.setValue(result.getAngles()[0]);
            angle3d.setValue(result.getAngles()[1]);
            timeLeft.setValue(result.getTimeLeft());
            Log.d("=====Exercise.STATUS=====", result.getPosition().name() );

            switch (position) {
                case RESTING:
                    statusText.setValue("Status: RESTING");
                    statusColor.setValue(Color.GREEN);
                    if (lastStatus != Exercise.STATUS.RESTING) {
                        Objects.requireNonNull(ttsHelper.getValue()).speak("RESTING");
                    }
                    break;

                case ALIGNED:
                    statusText.setValue("Status: ALIGNED");
                    statusColor.setValue(Color.BLUE);
                    if (lastStatus != Exercise.STATUS.ALIGNED) {
                        Objects.requireNonNull(ttsHelper.getValue()).speak("ALIGNED");
                    }
                    break;

                case TRANSITIONING:
                    statusText.setValue("Status: TRANSITIONING");
                    statusColor.setValue(Color.RED);
//                    if (lastStatus != Exercise.STATUS.TRANSITIONING) {
//                        Objects.requireNonNull(ttsHelper.getValue()).speak("TRANSITIONING");
//                    }
                    break;

                case REP_FINISHED:
                    if(!mediaPlayer.isPlaying()) {
                        mediaPlayer.start();
                    }
                    break;

                case FINISHED:
                    setExecutionState(RoutineConstants.EXECUTION_STATE.FINISHED);
                    break;
            }

            counter.setValue(result.getTotalRepetition());
        } else {
            statusText.setValue("Status: NO PERSON DETECTED!!");
//            statusText.setValue(exercise.getValue().getClass().getName());
            statusColor.setValue(Color.YELLOW);
        }
//        setExecutionState(RoutineConstants.EXECUTION_STATE.FINISHED);
    }

    public void init() {
        Objects.requireNonNull(exercise.getValue()).init();
        initPlayer();
    }

    public void initPlayer() {

        try (AssetFileDescriptor afd = context.getAssets().openFd("sounds/rep_finish.wav")) {
            mediaPlayer.setDataSource(afd.getFileDescriptor(), afd. getStartOffset(), afd.getLength());
            mediaPlayer.prepare();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    // FOR CLEARING RESOURCES
    @Override
    public void onCleared() {
        super.onCleared();
        if (ttsHelper.getValue()!= null) {
            ttsHelper.getValue().shutdown();
        }
    }
}