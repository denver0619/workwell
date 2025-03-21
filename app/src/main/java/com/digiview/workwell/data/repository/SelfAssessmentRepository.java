package com.digiview.workwell.data.repository;

import com.digiview.workwell.data.models.SelfAssessment;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.CompletableFuture;

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
    public CompletableFuture<SelfAssessment> getSelfAssessment(String selfAssessmentId) {
        CompletableFuture<SelfAssessment> future = new CompletableFuture<>();

        selfAssessmentCollection.document(selfAssessmentId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    SelfAssessment selfAssessment = documentSnapshot.toObject(SelfAssessment.class);
                    if (selfAssessment != null) {
                        future.complete(selfAssessment);
                    } else {
                        future.completeExceptionally(new Exception("SelfAssessment not found"));
                    }
                })
                .addOnFailureListener(future::completeExceptionally);

        return future;
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
