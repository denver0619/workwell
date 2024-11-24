package com.digiview.workwell.ui.routine.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.digiview.workwell.data.models.Routine;
import com.digiview.workwell.data.service.RoutineService;

import java.util.List;

public class RoutineViewModel extends ViewModel {
    private final MutableLiveData<List<Routine>> routineList = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>(null);

    private final RoutineService routineService;

    public RoutineViewModel() {
        this.routineService = new RoutineService();
    }

    public LiveData<List<Routine>> getRoutineList() {
        return routineList;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void fetchRoutines() {
        isLoading.setValue(true);
        errorMessage.setValue(null);

        routineService.getEnrichedRoutinesForUser()
                .thenAccept(routines -> {
                    routineList.postValue(routines);
                    isLoading.postValue(false);
                })
                .exceptionally(e -> {
                    isLoading.postValue(false);
                    errorMessage.postValue("Failed to fetch routines: " + e.getMessage());
                    return null;
                });
    }
}
