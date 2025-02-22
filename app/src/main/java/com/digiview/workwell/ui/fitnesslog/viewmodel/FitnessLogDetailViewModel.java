package com.digiview.workwell.ui.fitnesslog.viewmodel;

import android.util.Log;

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
            if (routine != null && routine.getExercises() != null) {
                Log.d("FitnessLogViewModel", "Fetched exercises: " + routine.getExercises().size());
                exercises.postValue(routine.getExercises());
            } else {
                Log.e("FitnessLogViewModel", "No exercises found for routineId: " + routineId);
            }
            isLoading.postValue(false);
        }).exceptionally(e -> {
            Log.e("FitnessLogViewModel", "Failed to fetch exercises: ", e);
            isLoading.postValue(false);
            return null;
        });
    }

}