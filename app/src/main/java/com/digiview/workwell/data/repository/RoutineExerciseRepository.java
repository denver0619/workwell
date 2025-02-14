package com.digiview.workwell.data.repository;

import com.digiview.workwell.data.models.Constraint;
import com.digiview.workwell.data.models.Exercise;
import com.digiview.workwell.data.models.Keypoint;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.*;
import java.util.ArrayList;
import java.util.List;

public class RoutineExerciseRepository {

    private final FirebaseFirestore db;

    public RoutineExerciseRepository() {
        this.db = FirebaseFirestore.getInstance();
    }

    /**
     * Fetch Exercise by ID from "exercises" collection.
     */
    public Task<Exercise> getExerciseById(String exerciseId) {
        return db.collection("exercises")
                .document(exerciseId)
                .get()
                .onSuccessTask(doc -> {
                    if (!doc.exists()) {
                        return Tasks.forException(new Exception("Exercise not found: " + exerciseId));
                    }
                    Exercise exercise = doc.toObject(Exercise.class);
                    return Tasks.forResult(exercise);
                });
    }

    /**
     * Fetch a Constraint by its ID from "constraints" collection.
     */
    public Task<Constraint> getConstraintById(String constraintId) {
        return db.collection("constraints")
                .document(constraintId)
                .get()
                .onSuccessTask(doc -> {
                    if (!doc.exists()) {
                        return Tasks.forException(new Exception("Constraint not found: " + constraintId));
                    }
                    Constraint constraint = doc.toObject(Constraint.class);
                    return Tasks.forResult(constraint);
                });
    }

    /**
     * Fetch multiple Keypoints by their IDs from "keypoints" collection.
     */
    public Task<List<Keypoint>> getKeypointsByIds(List<String> keypointIds) {
        // If the keypointIds list is null or empty, return an empty list.
        if (keypointIds == null || keypointIds.isEmpty()) {
            return Tasks.forResult(new ArrayList<>());
        }

        // Build a list of individual get() tasks.
        List<Task<DocumentSnapshot>> tasks = new ArrayList<>();
        for (String id : keypointIds) {
            Task<DocumentSnapshot> t = db.collection("keypoints")
                    .document(id)
                    .get();
            tasks.add(t);
        }
        // Wait for all keypoint docs to be fetched.
        return Tasks.whenAllSuccess(tasks)
                .onSuccessTask(results -> {
                    @SuppressWarnings("unchecked")
                    List<DocumentSnapshot> docs = (List<DocumentSnapshot>) (List<?>) results;
                    List<Keypoint> keypoints = new ArrayList<>();
                    for (DocumentSnapshot doc : docs) {
                        if (doc.exists()) {
                            Keypoint kp = doc.toObject(Keypoint.class);
                            keypoints.add(kp);
                        }
                    }
                    return Tasks.forResult(keypoints);
                });
    }

}
