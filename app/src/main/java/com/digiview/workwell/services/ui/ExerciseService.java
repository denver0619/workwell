package com.digiview.workwell.services.ui;

import com.digiview.workwell.models.Exercise;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ExerciseService {
    private final FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private final CollectionReference exercisesRef = firestore.collection("exercises");

    public CompletableFuture<Exercise> getExerciseById(String exerciseId) {
        CompletableFuture<Exercise> future = new CompletableFuture<>();

        exercisesRef.document(exerciseId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Exercise exercise = documentSnapshot.toObject(Exercise.class);
                        future.complete(exercise);
                    } else {
                        future.complete(null); // Exercise not found
                    }
                })
                .addOnFailureListener(future::completeExceptionally);

        return future;
    }
}

