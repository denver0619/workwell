package com.digiview.workwell.ui.routine.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.digiview.workwell.data.models.Routine;
import com.digiview.workwell.data.service.RoutineService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class RoutineViewModel extends ViewModel {
    private final MutableLiveData<List<Routine>> activeRoutines = new MutableLiveData<>();
    private final MutableLiveData<List<Routine>> inactiveRoutines = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>(null);

    private final RoutineService routineService;

    public RoutineViewModel() {
        this.routineService = new RoutineService();
    }

    public LiveData<List<Routine>> getActiveRoutines() {
        return activeRoutines;
    }

    public LiveData<List<Routine>> getInactiveRoutines() {
        return inactiveRoutines;
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
                    Date currentDate = new Date();
                    Log.d("RoutineDebug", "Current Date: " + currentDate);

                    List<Routine> activeList = new ArrayList<>();
                    List<Routine> inactiveList = new ArrayList<>();

                    for (Routine routine : routines) {
                        Log.d("RoutineDebug", "Processing Routine: " + routine.getName());
                        Log.d("RoutineDebug", "Start Date: " + routine.getStartDate());
                        Log.d("RoutineDebug", "End Date: " + routine.getEndDate());

                        if (routine.getStartDate() != null && routine.getEndDate() != null) {
                            if (!currentDate.before(routine.getStartDate()) && !currentDate.after(routine.getEndDate())) {
                                Log.d("RoutineDebug", "Routine is ACTIVE");
                                activeList.add(routine);
                            } else {
                                Log.d("RoutineDebug", "Routine is INACTIVE");
                                inactiveList.add(routine);
                            }
                        } else {
                            Log.d("RoutineDebug", "Routine has NULL dates, marking as INACTIVE");
                            inactiveList.add(routine);
                        }
                    }

                    Log.d("RoutineDebug", "Active Count: " + activeList.size());
                    Log.d("RoutineDebug", "Inactive Count: " + inactiveList.size());

                    activeRoutines.postValue(activeList);
                    inactiveRoutines.postValue(inactiveList);
                    isLoading.postValue(false);
                })
                .exceptionally(e -> {
                    Log.e("RoutineDebug", "Failed to process routines", e);
                    isLoading.postValue(false);
                    return null;
                });

    }
}
