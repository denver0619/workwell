package com.digiview.workwell.data.repository;

import com.digiview.workwell.data.models.SelfAssessment;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class SelfAssessmentRepository {

    private final CollectionReference selfAssessmentCollection;

    public SelfAssessmentRepository() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        selfAssessmentCollection = db.collection("selfassessments");
    }

    // Add a new self-assessment to Firestore
    public Task<Void> addSelfAssessment(SelfAssessment selfAssessment) {
        if (selfAssessment.getSelfAssessmentId() == null) {
            String newId = selfAssessmentCollection.document().getId();
            selfAssessment.setSelfAssessmentId(newId);
        }
        return selfAssessmentCollection.document(selfAssessment.getSelfAssessmentId()).set(selfAssessment);
    }

    // Retrieve a self-assessment from Firestore
    public Task<SelfAssessment> getSelfAssessment(String selfAssessmentId) {
        return selfAssessmentCollection.document(selfAssessmentId)
                .get()
                .continueWith(task -> task.getResult().toObject(SelfAssessment.class));
    }

    // Update an existing self-assessment in Firestore
    public Task<Void> updateSelfAssessment(SelfAssessment selfAssessment) {
        return selfAssessmentCollection.document(selfAssessment.getSelfAssessmentId()).set(selfAssessment);
    }

    // Delete a self-assessment from Firestore
    public Task<Void> deleteSelfAssessment(String selfAssessmentId) {
        return selfAssessmentCollection.document(selfAssessmentId).delete();
    }
}
