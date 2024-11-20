package com.digiview.workwell.ui.routine.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class RoutineViewModel extends ViewModel {
    private MutableLiveData<List<String>> routineList;

    public RoutineViewModel() {
        routineList = new MutableLiveData<>();

        List<String> dummyData = new ArrayList<>();
        dummyData.add("Lower Back\nRoutine 1");
        dummyData.add("Neck\nRoutine 1");
        dummyData.add("Shoulder\nRoutine 1");
        dummyData.add("Wrist\nRoutine 1");
        routineList.setValue(dummyData);
    }

    public LiveData<List<String>> getDataList() {
        return routineList;
    }

    public void addRoutine(String routine) {
        List<String> currentList = routineList.getValue();
        if (currentList != null) {
            currentList.add(routine);
            routineList.setValue(currentList); // Notify observers
        }
    }
}