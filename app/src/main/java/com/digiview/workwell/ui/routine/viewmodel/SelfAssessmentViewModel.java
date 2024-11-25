package com.digiview.workwell.ui.routine.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SelfAssessmentViewModel extends ViewModel {
    private final MutableLiveData<Integer> stiffness = new MutableLiveData<>(0);
    private final MutableLiveData<Integer> pain = new MutableLiveData<>(0);
    private final MutableLiveData<Integer> difficulty = new MutableLiveData<>(0);
    private final MutableLiveData<Integer> awareness = new MutableLiveData<>(0);

    public LiveData<Integer> getStiffness() {
        return stiffness;
    }

    public LiveData<Integer> getPain() {
        return pain;
    }

    public LiveData<Integer> getDifficulty() {
        return difficulty;
    }

    public LiveData<Integer> getAwareness() {
        return awareness;
    }

    public void setStiffness(int value) {
        stiffness.setValue(value);
    }

    public void setPain(int value) {
        pain.setValue(value);
    }

    public void setDifficulty(int value) {
        difficulty.setValue(value);
    }

    public void setAwareness(int value) {
        awareness.setValue(value);
    }

    public void submitData() {
        // Example: Prepare data to send to backend
        Log.d("SelfAssessment", "Stiffness: " + stiffness.getValue());
        Log.d("SelfAssessment", "Pain: " + pain.getValue());
        Log.d("SelfAssessment", "Difficulty: " + difficulty.getValue());
        Log.d("SelfAssessment", "Awareness: " + awareness.getValue());
    }
}
