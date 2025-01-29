package com.digiview.workwell.ui.routine.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.digiview.workwell.data.models.SelfAssessment;
import com.digiview.workwell.data.service.RoutineLogService;
import com.digiview.workwell.data.service.SelfAssessmentService;
import com.google.firebase.auth.FirebaseAuth;

import java.util.concurrent.CompletableFuture;

public class SelfAssessmentViewModel extends ViewModel {
    private final MutableLiveData<Integer> stiffness = new MutableLiveData<>(0);
    private final MutableLiveData<Integer> pain = new MutableLiveData<>(0);
    private final MutableLiveData<Integer> difficulty = new MutableLiveData<>(0);
    private final MutableLiveData<Integer> awareness = new MutableLiveData<>(0);
    private final SelfAssessmentService selfAssessmentService = new SelfAssessmentService();
    private final RoutineLogService routineLogService = new RoutineLogService();

    public LiveData<Integer> getStiffness() { return stiffness; }
    public LiveData<Integer> getPain() { return pain; }
    public LiveData<Integer> getDifficulty() { return difficulty; }
    public LiveData<Integer> getAwareness() { return awareness; }

    public void setStiffness(int value) { stiffness.setValue(value); }
    public void setPain(int value) { pain.setValue(value); }
    public void setDifficulty(int value) { difficulty.setValue(value); }
    public void setAwareness(int value) { awareness.setValue(value); }

    public void submitData(String videoId, String routineId, String routineLogName, String uid) {
        SelfAssessment selfAssessment = new SelfAssessment();
        selfAssessment.setStiffness(stiffness.getValue() != null ? stiffness.getValue() : 0);
        selfAssessment.setPain(pain.getValue() != null ? pain.getValue() : 0);
        selfAssessment.setDifficulty(difficulty.getValue() != null ? difficulty.getValue() : 0);
        selfAssessment.setAwareness(awareness.getValue() != null ? awareness.getValue() : 0);

        selfAssessmentService.addSelfAssessment(selfAssessment)
                .thenAccept(selfAssessmentId -> {
                    Log.d("SelfAssessmentViewModel", "SelfAssessment submitted with ID: " + selfAssessmentId);

                    // Now, create the RoutineLog with the selfAssessmentId and videoId
                    routineLogService.createRoutineLog(videoId, selfAssessmentId, routineId, routineLogName, uid)
                            .thenAccept(routineLogId -> {
                                Log.d("SelfAssessmentViewModel", "RoutineLog created with ID: " + routineLogId);
                            })
                            .exceptionally(e -> {
                                Log.e("SelfAssessmentViewModel", "Error creating RoutineLog", e);
                                return null;
                            });
                })
                .exceptionally(e -> {
                    Log.e("SelfAssessmentViewModel", "Error submitting SelfAssessment", e);
                    return null;
                });
    }
}
