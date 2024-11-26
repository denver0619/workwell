package com.digiview.workwell.data.repository;

import com.digiview.workwell.data.models.RoutineLogs;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.CompletableFuture;

public class RoutineLogRepository {

    private final FirebaseFirestore firestore;

    public RoutineLogRepository() {
        this.firestore = FirebaseFirestore.getInstance();
    }

    /**
     * Save a RoutineLog to Firestore and return the generated document ID.
     */
    public CompletableFuture<String> createRoutineLog(RoutineLogs routineLog) {
        CompletableFuture<String> future = new CompletableFuture<>();

        // Generate a new document reference to get the ID
        DocumentReference documentReference = firestore.collection("routinelogs").document();
        String generatedId = documentReference.getId();

        // Set the generated ID into the routineLog object
        routineLog.setRoutineLogId(generatedId);

        // Save the object to Firestore with the document ID
        documentReference.set(routineLog)
                .addOnSuccessListener(unused -> future.complete(generatedId))
                .addOnFailureListener(future::completeExceptionally);

        return future;
    }

    public Task<Void> updateRoutineLogField(String routineLogId, String fieldName, Object value) {
        return firestore.collection("routinelogs")
                .document(routineLogId)
                .update(fieldName, value);
    }
    public Task<Void> updateVideoIdField(String routineLogId, String videoId) {
        return firestore.collection("routinelogs")
                .document(routineLogId)
                .update("VideoId", videoId);
    }




}
