package com.digiview.workwell.services.ui;

import android.util.Log;

import com.digiview.workwell.models.Routine;
import com.digiview.workwell.models.RoutineExercise;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class RoutineService {

    private final FirebaseFirestore firestore;
    private final CollectionReference routinesRef;

    public RoutineService() {
        firestore = FirebaseFirestore.getInstance();
        routinesRef = firestore.collection("routines");
    }

    public CompletableFuture<List<Routine>> fetchRoutinesForCurrentUser() {
        CompletableFuture<List<Routine>> future = new CompletableFuture<>();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        routinesRef.whereEqualTo("AssignedTo", userId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Routine> routines = new ArrayList<>();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        Routine routine = doc.toObject(Routine.class);
                        routines.add(routine);
                    }
                    future.complete(routines);
                })
                .addOnFailureListener(e -> {
                    Log.e("RoutineService", "Error fetching routines: " + e.getMessage(), e);
                    future.completeExceptionally(e);
                });

        return future;
    }

    public CompletableFuture<Routine> fetchRoutineDetails(Routine routine) {
        CompletableFuture<Routine> future = new CompletableFuture<>();

        routine.fetchAssignedName().thenAccept(assignedName -> {
            routine.setAssignedName(assignedName);
            List<RoutineExercise> exercises = routine.getExercises();
            if (exercises != null && !exercises.isEmpty()) {
                CompletableFuture<?>[] exerciseFutures = exercises.stream()
                        .map(this::fetchExerciseDetails)
                        .toArray(CompletableFuture[]::new);
                CompletableFuture.allOf(exerciseFutures).thenRun(() -> future.complete(routine));
            } else {
                Log.d("RoutineService", "No exercises found for routine: " + routine.getName());
                future.complete(routine);
            }
        }).exceptionally(e -> {
            Log.e("RoutineService", "Error fetching assigned name: " + e.getMessage(), e);
            future.completeExceptionally(e);
            return null;
        });

        return future;
    }

    private CompletableFuture<Void> fetchExerciseDetails(RoutineExercise exercise) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        DocumentReference exerciseRef = firestore.collection("exercises").document(exercise.getExerciseId());

        exerciseRef.get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        exercise.setExerciseName(documentSnapshot.getString("Name"));
                        exercise.setExerciseDescription(documentSnapshot.getString("Description"));
                    } else {
                        Log.d("RoutineService", "Exercise not found with ID: " + exercise.getExerciseId());
                    }
                    future.complete(null);
                })
                .addOnFailureListener(e -> {
                    Log.e("RoutineService", "Error fetching exercise details: " + e.getMessage(), e);
                    future.completeExceptionally(e);
                });

        return future;
    }

    public CompletableFuture<List<Routine>> fetchAndManipulateRoutinesForCurrentUser() {
        return fetchRoutinesForCurrentUser().thenCompose(routines -> {
            CompletableFuture<?>[] routineFutures = routines.stream()
                    .map(this::fetchRoutineDetails)
                    .toArray(CompletableFuture[]::new);

            return CompletableFuture.allOf(routineFutures).thenApply(v -> routines);
        });
    }
}
