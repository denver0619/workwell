package com.digiview.workwell.data.repository;

import com.digiview.workwell.data.models.Exercise;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.CompletableFuture;

public class ExerciseRepository {
    private final FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private final CollectionReference exercisesRef = firestore.collection("exercises");

    /**
     * Fetch an exercise by its ID.
     *
     * @param exerciseId The ID of the exercise to fetch.
     * @return A CompletableFuture containing the Exercise object or null if not found.
     */
    public CompletableFuture<Exercise> getExerciseById(String exerciseId) {
        CompletableFuture<Exercise> future = new CompletableFuture<>();

        // Query the Firestore document for the given exercise ID
        exercisesRef.document(exerciseId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Exercise exercise = documentSnapshot.toObject(Exercise.class);
                        future.complete(exercise); // Return the Exercise object
                    } else {
                        future.complete(null); // Exercise not found
                    }
                })
                .addOnFailureListener(future::completeExceptionally); // Handle errors

        return future;
    }
}
