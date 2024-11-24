package com.digiview.workwell.ui.routine.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.digiview.workwell.models.Routine;
import com.digiview.workwell.models.RoutineExercise;

import java.util.ArrayList;
import java.util.List;

public class RoutineDetailViewModel extends ViewModel {
    private final MutableLiveData<List<RoutineExercise>> routineDetailList;
    private Routine currentRoutine; // Store the current Routine object

    public RoutineDetailViewModel() {
        routineDetailList = new MutableLiveData<>(new ArrayList<>());
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

    // Add an exercise to the routine
    public void addExercise(RoutineExercise exercise) {
        List<RoutineExercise> currentList = routineDetailList.getValue();
        if (currentList != null) {
            currentList.add(exercise);
            routineDetailList.setValue(currentList); // Notify observers
        }
    }
}
