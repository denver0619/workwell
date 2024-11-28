package com.digiview.workwell.ui.fitnesslog.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.digiview.workwell.data.models.Routine;
import com.digiview.workwell.data.models.RoutineExercise;
import com.digiview.workwell.data.service.RoutineService;

import java.util.List;

public class FitnessLogDetailViewModel extends ViewModel {
    private final RoutineService routineService;

    private final MutableLiveData<List<RoutineExercise>> exercises = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);

    public FitnessLogDetailViewModel() {
        routineService = new RoutineService();
    }

    public LiveData<List<RoutineExercise>> getExercises() {
        return exercises;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public void fetchRoutineExercises(String routineId) {
        isLoading.setValue(true);
        routineService.getRoutineById(routineId).thenAccept(routine -> {
            exercises.postValue(routine.getExercises());
            isLoading.postValue(false);
        }).exceptionally(e -> {
            isLoading.postValue(false);
            return null;
        });
    }
}