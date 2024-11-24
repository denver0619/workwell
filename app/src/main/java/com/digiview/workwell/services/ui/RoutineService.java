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
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class RoutineService {
    private final FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private final CollectionReference routinesRef = firestore.collection("routines");
    private final CollectionReference usersRef = firestore.collection("users");
    private final ExerciseService exerciseService = new ExerciseService();
    private final String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

    // Fetch routines for the current logged-in user with exercise details
    public CompletableFuture<List<Routine>> getUserRoutinesWithDetails() {
        FirebaseAuth.getInstance().getCurrentUser().getIdToken(true)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Map<String, Object> claims = task.getResult().getClaims();
                        Log.d("AuthDebug", "Claims: " + claims.toString());
                    } else {
                        Log.e("AuthDebug", "Failed to fetch claims: " + task.getException().getMessage());
                    }
                });

        return getUserRoutineIds() // Fetch routine IDs from the user document
                .thenCompose(this::getRoutinesByIds) // Fetch routines by IDs
                .thenCompose(routines -> {
                    List<CompletableFuture<Routine>> updatedRoutines = new ArrayList<>();

                    for (Routine routine : routines) {
                        updatedRoutines.add(enrichRoutineExercises(routine));
                    }

                    return CompletableFuture.allOf(updatedRoutines.toArray(new CompletableFuture[0]))
                            .thenApply(v -> {
                                List<Routine> enrichedRoutines = new ArrayList<>();
                                for (CompletableFuture<Routine> routineFuture : updatedRoutines) {
                                    enrichedRoutines.add(routineFuture.join());
                                }
                                return enrichedRoutines;
                            });
                });
    }

    // Fetch routine IDs from the current user's document
    private CompletableFuture<List<String>> getUserRoutineIds() {
        CompletableFuture<List<String>> future = new CompletableFuture<>();
        DocumentReference userDoc = usersRef.document(currentUserId);

        userDoc.get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists() && documentSnapshot.contains("Routines")) {
                        List<String> routineIds = (List<String>) documentSnapshot.get("Routines");
                        future.complete(routineIds != null ? routineIds : new ArrayList<>());
                    } else {
                        future.complete(new ArrayList<>()); // No routines for this user
                    }
                })
                .addOnFailureListener(future::completeExceptionally);

        return future;
    }

    // Fetch routines by their IDs
    private CompletableFuture<List<Routine>> getRoutinesByIds(List<String> routineIds) {
        CompletableFuture<List<Routine>> future = new CompletableFuture<>();
        Log.d("FirestoreDebug", "Routine IDs: " + routineIds);

        if (routineIds.isEmpty()) {
            future.complete(new ArrayList<>());
            return future;
        }

        // Query to fetch routines where the current user ID is in the "Users" array
        routinesRef.whereArrayContains("Users", currentUserId)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    List<Routine> routines = new ArrayList<>();
                    for (QueryDocumentSnapshot document : querySnapshot) {
                        Log.d("RoutineService", "Fetched routine: " + document.getData());
                        Routine routine = document.toObject(Routine.class);
                        routine.setRoutineId(document.getId());
                        routines.add(routine);
                    }
                    future.complete(routines);
                })
                .addOnFailureListener(e -> {
                    Log.e("RoutineService", "Failed to fetch routines: ", e);
                    future.completeExceptionally(e);
                });

        return future;
    }




    // Enrich routine exercises with additional exercise details
    private CompletableFuture<Routine> enrichRoutineExercises(Routine routine) {
        List<CompletableFuture<Void>> tasks = new ArrayList<>();

        for (RoutineExercise routineExercise : routine.getExercises()) {
            tasks.add(exerciseService.getExerciseById(routineExercise.getExerciseId())
                    .thenAccept(exercise -> {
                        if (exercise != null) {
                            routineExercise.setExerciseName(exercise.getName());
                            routineExercise.setExerciseDescription(exercise.getDescription());
                        }
                    }));
        }

        return CompletableFuture.allOf(tasks.toArray(new CompletableFuture[0]))
                .thenApply(v -> routine);
    }
}

