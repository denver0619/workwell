package com.digiview.workwell.data.repository;

import com.digiview.workwell.data.models.Diagnosis;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class DiagnosisRepository {

    private final CollectionReference diagnosisRef;

    public DiagnosisRepository() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        diagnosisRef = db.collection("diagnoses");
    }

    /**
     * Fetch a diagnosis by its ID using CompletableFuture.
     *
     * @param diagnosisId The ID of the diagnosis to fetch.
     * @return A CompletableFuture containing the fetched diagnosis.
     */
    public CompletableFuture<Diagnosis> getDiagnosis(String diagnosisId) {
        CompletableFuture<Diagnosis> future = new CompletableFuture<>();
        diagnosisRef.document(diagnosisId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    Diagnosis diagnosis = documentSnapshot.toObject(Diagnosis.class);
                    future.complete(diagnosis);
                })
                .addOnFailureListener(future::completeExceptionally);
        return future;
    }

    public CompletableFuture<List<Diagnosis>> getAllDiagnosis() {
        CompletableFuture<List<Diagnosis>> future = new CompletableFuture<>();
        diagnosisRef.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Diagnosis> diagnosisList = new ArrayList<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Diagnosis diagnosis = document.toObject(Diagnosis.class);
                        diagnosisList.add(diagnosis);
                    }
                    future.complete(diagnosisList);
                })
                .addOnFailureListener(future::completeExceptionally);
        return future;
    }
}
