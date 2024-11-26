package com.digiview.workwell.ui.routine.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.digiview.workwell.data.models.SelfAssessment;
import com.digiview.workwell.data.service.RoutineLogService;
import com.digiview.workwell.data.service.SelfAssessmentService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;

public class SelfAssessmentViewModel extends ViewModel {
    private final MutableLiveData<Integer> stiffness = new MutableLiveData<>(0);
    private final MutableLiveData<Integer> pain = new MutableLiveData<>(0);
    private final MutableLiveData<Integer> difficulty = new MutableLiveData<>(0);
    private final MutableLiveData<Integer> awareness = new MutableLiveData<>(0);
    private final SelfAssessmentService selfAssessmentService = new SelfAssessmentService();

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

    public void submitData(String routineLogId) {

        SelfAssessment selfAssessment = new SelfAssessment();
        selfAssessment.setRoutineLogId(routineLogId);
        selfAssessment.setStiffness(stiffness.getValue() != null ? stiffness.getValue() : 0);
        selfAssessment.setPain(pain.getValue() != null ? pain.getValue() : 0);
        selfAssessment.setDifficulty(difficulty.getValue() != null ? difficulty.getValue() : 0);
        selfAssessment.setAwareness(awareness.getValue() != null ? awareness.getValue() : 0);

        selfAssessmentService.addSelfAssessment(selfAssessment).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String selfAssessmentId = selfAssessment.getSelfAssessmentId();
                Log.d("SelfAssessmentViewModel", "SelfAssessment submitted successfully.");

                // Update RoutineLog with the new SelfAssessmentId
                RoutineLogService routineLogService = new RoutineLogService();
                routineLogService.updateRoutineLogSelfAssessment(routineLogId, selfAssessmentId)
                        .addOnCompleteListener(updateTask -> {
                            if (updateTask.isSuccessful()) {
                                Log.d("SelfAssessmentViewModel", "RoutineLog updated with SelfAssessmentId.");
                            } else {
                                Log.e("SelfAssessmentViewModel", "Error updating RoutineLog: ", updateTask.getException());
                            }
                        });
            } else {
                Log.e("SelfAssessmentViewModel", "Error submitting SelfAssessment: ", task.getException());
            }
        });
    }
}
