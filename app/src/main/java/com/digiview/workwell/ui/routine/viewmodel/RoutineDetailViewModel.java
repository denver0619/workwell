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

    public RoutineDetailViewModel() {
        routineDetailList = new MutableLiveData<>(new ArrayList<>());
    }

    public LiveData<List<RoutineExercise>> getDataList() {
        return routineDetailList;
    }

    public void setRoutine(Routine routine) {
        if (routine != null && routine.getExercises() != null) {
            routineDetailList.setValue(routine.getExercises()); // Update the LiveData with exercises
        }
    }

    public void addExercise(RoutineExercise exercise) {
        List<RoutineExercise> currentList = routineDetailList.getValue();
        if (currentList != null) {
            currentList.add(exercise);
            routineDetailList.setValue(currentList); // Notify observers
        }
    }
}
