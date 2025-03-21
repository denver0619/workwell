package com.digiview.workwell.data.repository;

import com.digiview.workwell.data.models.RoutineLogs;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class RoutineLogRepository {

    private final FirebaseFirestore firestore;
    private final CollectionReference routineLogsRef;

    public RoutineLogRepository() {
        this.firestore = FirebaseFirestore.getInstance();
        routineLogsRef = firestore.collection("routinelogs");
    }

    /**
     * Save a RoutineLog to Firestore and return the generated document ID.
     */
//    public CompletableFuture<String> createRoutineLog(RoutineLogs routineLog) {
//        CompletableFuture<String> future = new CompletableFuture<>();
//
//        // Generate a new document reference to get the ID
//        DocumentReference documentReference = firestore.collection("routinelogs").document();
//        String generatedId = documentReference.getId();
//
//        // Set the generated ID into the routineLog object
//        routineLog.setRoutineLogId(generatedId);
//
//        // Create a map to explicitly include only required fields
//        Map<String, Object> data = new HashMap<>();
//        data.put("RoutineLogId", routineLog.getRoutineLogId());
//        data.put("RoutineId", routineLog.getRoutineId());
//        data.put("RoutineLogName", routineLog.getRoutineLogName());
//        data.put("OrganizationId", routineLog.getOrganizationId());
//        data.put("Uid", routineLog.getUid());
//        data.put("SelfAssessmentId", routineLog.getSelfAssessmentId());
//        data.put("VideoId", routineLog.getVideoId());
//        data.put("JournalId", routineLog.getJournalId());
//        data.put("CreatedAt", routineLog.getCreatedAt());
//
//        // Save the map to Firestore with the document ID
//        documentReference.set(data)
//                .addOnSuccessListener(unused -> future.complete(generatedId))
//                .addOnFailureListener(future::completeExceptionally);
//
//        return future;
//    }


    public CompletableFuture<String> createRoutineLog(RoutineLogs routineLog) {
        CompletableFuture<String> future = new CompletableFuture<>();

        // Generate a new document reference to get the ID
        DocumentReference documentReference = firestore.collection("routinelogs").document();
        String generatedId = documentReference.getId();

        // Set the generated ID into the routineLog object
        routineLog.setRoutineLogId(generatedId);


        // Create a map to explicitly include only required fields
        Map<String, Object> data = new HashMap<>();
        data.put("RoutineLogId", generatedId);
        data.put("RoutineId", routineLog.getRoutineId());
        data.put("RoutineLogName", routineLog.getRoutineLogName());
        data.put("OrganizationId", routineLog.getOrganizationId());
        data.put("Uid", routineLog.getUid());
        data.put("SelfAssessmentId", routineLog.getSelfAssessmentId());
        data.put("VideoId", routineLog.getVideoId());
        data.put("JournalId", routineLog.getJournalId());
        data.put("CreatedAt", routineLog.getCreatedAt());

        // Save the map to Firestore with the document ID
        documentReference.set(data)
                .addOnSuccessListener(unused -> future.complete(generatedId))
                .addOnFailureListener(future::completeExceptionally);

        return future;
    }

    public Task<Void> updateSelfAssessmentField(String routineLogId, String selfAssessmentId) {
        return firestore.collection("routinelogs")
                .document(routineLogId)
                .update("SelfAssessmentId", selfAssessmentId);
    }
    public Task<Void> updateVideoIdField(String routineLogId, String videoId) {
        return firestore.collection("routinelogs")
                .document(routineLogId)
                .update("VideoId", videoId);
    }
    public Task<Void> updateJournalIdField(String routineLogId, String journalId) {
        return firestore.collection("routinelogs")
                .document(routineLogId)
                .update("JournalId", journalId);
    }



    public CompletableFuture<List<RoutineLogs>> fetchRoutineLogsForCurrentUser(String currentUserId) {
        CompletableFuture<List<RoutineLogs>> future = new CompletableFuture<>();

        routineLogsRef.whereEqualTo("Uid", currentUserId)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    List<RoutineLogs> routineLogs = new ArrayList<>();
                    if (querySnapshot != null) {
                        for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                            RoutineLogs log = document.toObject(RoutineLogs.class);
                            if (log != null) {
                                routineLogs.add(log);
                            }
                        }
                    }
                    future.complete(routineLogs);
                })
                .addOnFailureListener(future::completeExceptionally);

        return future;
    }

}
