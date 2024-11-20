package com.digiview.workwell.ui.routine.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class RoutineDetailViewModel extends ViewModel {
    private MutableLiveData<List<String>> routineDetailList;

    public RoutineDetailViewModel() {
        routineDetailList = new MutableLiveData<>();

        List<String> dummyData = new ArrayList<>();
        dummyData.add("Pelvic Tilts");
        dummyData.add("Cat-Cow\nStretch");
        dummyData.add("Knee-to-Chest\nStretch");
        dummyData.add("Sphinx stretch");
        routineDetailList.setValue(dummyData);
    }

    public LiveData<List<String>> getDataList() {
        return routineDetailList;
    }

    public void addRoutine(String routine) {
        List<String> currentList = routineDetailList.getValue();
        if (currentList != null) {
            currentList.add(routine);
            routineDetailList.setValue(currentList); // Notify observers
        }
    }
}