package com.digiview.workwell.ui.routine.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.digiview.workwell.models.Routine;
import com.digiview.workwell.services.ui.RoutineService;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class RoutineViewModel extends ViewModel {

    private final MutableLiveData<List<Routine>> routineList;
    private final MutableLiveData<Boolean> isLoading; // Optional: To track loading state
    private final RoutineService routineService;
    private final MutableLiveData<String> errorMessage;

    public RoutineViewModel() {
        routineService = new RoutineService();
        routineList = new MutableLiveData<>();
        isLoading = new MutableLiveData<>(false);
        errorMessage = new MutableLiveData<>(null);
    }

    public LiveData<List<Routine>> getRoutineList() {
        return routineList;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading; // Optional: Expose loading state
    }

    public void fetchRoutines() {
        isLoading.setValue(true); // Set loading state to true
        errorMessage.setValue(null); // Clear previous errors

        routineService.getUserRoutinesWithDetails()
                .thenAccept(routines -> {
                    routineList.postValue(routines); // Update the LiveData with enriched routines
                    isLoading.postValue(false); // Set loading state to false
                })
                .exceptionally(e -> {
                    isLoading.postValue(false); // Set loading state to false on failure
                    errorMessage.postValue("Failed to fetch routines: " + e.getMessage()); // Update error message
                    return null;
                });
    }


    public void addRoutine(Routine routine) {
        List<Routine> currentList = routineList.getValue();
        if (currentList != null) {
            currentList.add(routine);
            routineList.setValue(currentList); // Notify observers
        }
    }
}
