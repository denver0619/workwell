package com.digiview.workwell.data.repository;

import com.digiview.workwell.data.service.ExerciseService;
import com.digiview.workwell.data.models.Routine;
import com.digiview.workwell.data.models.RoutineExercise;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class RoutineRepository {
    // Firestore database instance
    private final FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    // References to Firestore collections for routines and users
    private final CollectionReference routinesRef = firestore.collection("routines");
    private final CollectionReference usersRef = firestore.collection("users");

    // ExerciseService used to fetch exercise details
    private final ExerciseService exerciseService = new ExerciseService();

    // Current logged-in user ID
    private final String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

    /**
     * Fetch all routines for the currently logged-in user, including enriched exercise details.
     *
     * Workflow:
     * 1. Get the routine IDs assigned to the user from their user document.
     * 2. Fetch the routines from the "routines" collection using the IDs.
     * 3. Enrich each routine with exercise details by fetching them from the service layer.
     *
     * @return A CompletableFuture containing a list of enriched Routine objects.
     */
    public CompletableFuture<List<Routine>> getUserRoutinesWithDetails() {
        return getUserRoutineIds() // Step 1: Get routine IDs
                .thenCompose(this::getRoutinesByIds) // Step 2: Fetch routines by IDs
                .thenCompose(routines -> {
                    // Step 3: Enrich routines with exercise details
                    List<CompletableFuture<Routine>> enrichedRoutines = new ArrayList<>();
                    for (Routine routine : routines) {
                        enrichedRoutines.add(enrichRoutineExercises(routine));
                    }

                    // Combine all enrichment tasks and return the enriched routines
                    return CompletableFuture.allOf(enrichedRoutines.toArray(new CompletableFuture[0]))
                            .thenApply(v -> {
                                List<Routine> result = new ArrayList<>();
                                for (CompletableFuture<Routine> future : enrichedRoutines) {
                                    result.add(future.join()); // Collect completed routines
                                }
                                return result;
                            });
                });
    }

    /**
     * Fetch the list of routine IDs assigned to the currently logged-in user.
     * The user's document contains a "Routines" field with an array of routine IDs.
     *
     * @return A CompletableFuture containing a list of routine IDs.
     */
    private CompletableFuture<List<String>> getUserRoutineIds() {
        CompletableFuture<List<String>> future = new CompletableFuture<>();
        DocumentReference userDoc = usersRef.document(currentUserId); // Reference to the user's document

        userDoc.get()
                .addOnSuccessListener(documentSnapshot -> {
                    // Check if the document exists and contains the "Routines" field
                    if (documentSnapshot.exists() && documentSnapshot.contains("Routines")) {
                        // Retrieve the list of routine IDs
                        List<String> routineIds = (List<String>) documentSnapshot.get("Routines");
                        future.complete(routineIds != null ? routineIds : new ArrayList<>());
                    } else {
                        // If no routines are found, return an empty list
                        future.complete(new ArrayList<>());
                    }
                })
                .addOnFailureListener(future::completeExceptionally); // Handle errors

        return future;
    }

    /**
     * Fetch the routines from the "routines" collection using the list of routine IDs.
     * Only routines where the current user is in the "Users" field are considered.
     *
     * @param routineIds List of routine IDs to fetch.
     * @return A CompletableFuture containing a list of Routine objects.
     */
    private CompletableFuture<List<Routine>> getRoutinesByIds(List<String> routineIds) {
        CompletableFuture<List<Routine>> future = new CompletableFuture<>();

        if (routineIds.isEmpty()) {
            // If no routine IDs are provided, return an empty list
            future.complete(new ArrayList<>());
            return future;
        }

        // Query routines where the current user ID is in the "Users" array
        routinesRef.whereArrayContains("Users", currentUserId)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    List<Routine> routines = new ArrayList<>();
                    for (QueryDocumentSnapshot document : querySnapshot) {
                        // Convert each Firestore document into a Routine object
                        Routine routine = document.toObject(Routine.class);
                        routine.setRoutineId(document.getId()); // Set the routine's ID
                        routines.add(routine);
                    }
                    future.complete(routines); // Return the list of routines
                })
                .addOnFailureListener(future::completeExceptionally); // Handle errors

        return future;
    }

    /**
     * Enrich a routine by fetching and adding details for its exercises.
     * Each exercise in the routine is updated with its name and description.
     *
     * @param routine The Routine object to enrich.
     * @return A CompletableFuture containing the enriched Routine object.
     */
    private CompletableFuture<Routine> enrichRoutineExercises(Routine routine) {
        List<CompletableFuture<Void>> tasks = new ArrayList<>();

        // Iterate through each exercise in the routine
        for (RoutineExercise exercise : routine.getExercises()) {
            // Use the service layer to fetch exercise details
            tasks.add(exerciseService.getExerciseById(exercise.getExerciseId())
                    .thenAccept(details -> {
                        if (details != null) {
                            // Update the exercise with its fetched details
                            exercise.setExerciseName(details.getName());
                            exercise.setExerciseDescription(details.getDescription());
                            exercise.setExerciseDeviceSetup(details.getDeviceSetup());
                            exercise.setVideoId(details.getVideoId());
                        }
                    }));
        }

        // Wait for all enrichment tasks to complete and return the enriched routine
        return CompletableFuture.allOf(tasks.toArray(new CompletableFuture[0]))
                .thenApply(v -> routine);
    }

    // Fetch a routine by its RoutineId
    public CompletableFuture<Routine> fetchRoutineById(String routineId) {
        CompletableFuture<Routine> future = new CompletableFuture<>();

        firestore.collection("routines")
                .document(routineId)
                .get()
                .addOnSuccessListener(document -> {
                    if (document.exists()) {
                        Routine routine = document.toObject(Routine.class);

                        // Enrich the routine with detailed exercise information
                        if (routine != null && routine.getExercises() != null) {
                            enrichRoutineExercises(routine)
                                    .thenAccept(future::complete)
                                    .exceptionally(e -> {
                                        future.completeExceptionally(e);
                                        return null;
                                    });
                        } else {
                            future.complete(routine); // Complete if there are no exercises
                        }
                    } else {
                        future.completeExceptionally(new Exception("Routine not found."));
                    }
                })
                .addOnFailureListener(future::completeExceptionally);

        return future;
    }
}
