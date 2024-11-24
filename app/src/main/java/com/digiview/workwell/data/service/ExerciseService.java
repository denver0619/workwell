package com.digiview.workwell.data.service;

import com.digiview.workwell.data.models.Exercise;
import com.digiview.workwell.data.repository.ExerciseRepository;

import java.util.concurrent.CompletableFuture;

public class ExerciseService {
    private final ExerciseRepository exerciseRepository;

    public ExerciseService() {
        this.exerciseRepository = new ExerciseRepository();
    }

    /**
     * Fetch an exercise by its ID.
     *
     * This method uses the repository layer to fetch the exercise
     * and can include additional processing if needed in the future.
     *
     * @param exerciseId The ID of the exercise to fetch.
     * @return A CompletableFuture containing the Exercise object or null if not found.
     */
    public CompletableFuture<Exercise> getExerciseById(String exerciseId) {
        return exerciseRepository.getExerciseById(exerciseId);
    }

    // Additional business logic methods can be added here
}
