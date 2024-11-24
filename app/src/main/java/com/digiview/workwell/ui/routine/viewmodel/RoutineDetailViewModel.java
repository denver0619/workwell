package com.digiview.workwell.ui.routine.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.digiview.workwell.data.models.Routine;
import com.digiview.workwell.data.models.RoutineExercise;
import com.digiview.workwell.data.service.RoutineLogService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class RoutineDetailViewModel extends ViewModel {
    private final MutableLiveData<List<RoutineExercise>> routineDetailList;
    private Routine currentRoutine; // Store the current Routine object
    private final RoutineLogService routineLogService; // Service for RoutineLogs

    public RoutineDetailViewModel() {
        routineDetailList = new MutableLiveData<>(new ArrayList<>());
        routineLogService = new RoutineLogService();
    }

    // LiveData for observing the list of exercises
    public LiveData<List<RoutineExercise>> getDataList() {
        return routineDetailList;
    }

    // Set the routine and update the exercise list
    public void setRoutine(Routine routine) {
        this.currentRoutine = routine; // Store the routine
        if (routine != null && routine.getExercises() != null) {
            routineDetailList.setValue(routine.getExercises()); // Update the LiveData with exercises
        }
    }

    // Get the current routine
    public Routine getRoutine() {
        return currentRoutine; // Return the stored routine
    }

    // Prepare a list of RoutineExercise objects
    public List<RoutineExercise> getRoutineExercises() {
        if (currentRoutine != null && currentRoutine.getExercises() != null) {
            return new ArrayList<>(currentRoutine.getExercises()); // Return a copy of the exercises
        }
        return new ArrayList<>(); // Return an empty list if no exercises are found
    }

    /**
     * Creates a RoutineLog in Firebase using the RoutineLogService.
     *
     * @param uid The user ID of the creator.
     * @return A CompletableFuture containing the generated RoutineLogId or an exception.
     */
    public CompletableFuture<String> createRoutineLog(String uid) {
        if (currentRoutine == null) {
            CompletableFuture<String> future = new CompletableFuture<>();
            future.completeExceptionally(new IllegalStateException("Routine is not set."));
            return future;
        }

        return routineLogService.createRoutineLog(
                currentRoutine.getRoutineId(),
                currentRoutine.getName(),
                uid
        );
    }
}
