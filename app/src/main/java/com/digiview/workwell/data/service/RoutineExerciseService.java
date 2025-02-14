package com.digiview.workwell.data.service;

import com.digiview.workwell.data.models.RoutineExerciseDetailDTO;
import com.digiview.workwell.data.models.RoutineExerciseDetailDTO.ConstraintDetailDTO;
import com.digiview.workwell.data.models.Constraint;
import com.digiview.workwell.data.models.RoutineExercise;
import com.digiview.workwell.data.repository.RoutineExerciseRepository;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;

import java.util.ArrayList;
import java.util.List;

public class RoutineExerciseService {

    private final RoutineExerciseRepository repository;

    public RoutineExerciseService() {
        this.repository = new RoutineExerciseRepository();
    }

    /**
     * Build a composite DTO containing:
     *  - RoutineExercise fields
     *  - The full Exercise details (which includes a list of Constraint IDs)
     *  - For each Constraint (fetched using those IDs), its Keypoints are fetched.
     */
    public Task<RoutineExerciseDetailDTO> getRoutineExerciseDetail(RoutineExercise routineExercise) {
        // 1. Fetch the Exercise document from "exercises" collection.
        return repository.getExerciseById(routineExercise.getExerciseId())
                .onSuccessTask(exercise -> {
                    // Build partial DTO from routineExercise fields and the full Exercise object.
                    RoutineExerciseDetailDTO dto = new RoutineExerciseDetailDTO();
                    dto.setExerciseId(routineExercise.getExerciseId());
                    dto.setReps(routineExercise.getReps());
                    dto.setDuration(routineExercise.getDuration());
                    dto.setExerciseName(routineExercise.getExerciseName());
                    dto.setExerciseDescription(routineExercise.getExerciseDescription());
                    dto.setExercise(exercise);

                    // 2. Get the list of Constraint IDs from the Exercise.
                    List<String> constraintIds = exercise.getConstraints();
                    if (constraintIds == null || constraintIds.isEmpty()) {
                        // No constraints: return the DTO with an empty constraints list.
                        dto.setConstraints(new ArrayList<>());
                        return Tasks.forResult(dto);
                    }

                    // 3. For each Constraint ID, fetch its Constraint document.
                    List<Task<Constraint>> constraintTasks = new ArrayList<>();
                    for (String constraintId : constraintIds) {
                        Task<Constraint> constraintTask = repository.getConstraintById(constraintId);
                        constraintTasks.add(constraintTask);
                    }

                    // Wait until all Constraint documents are fetched.
                    return Tasks.whenAllSuccess(constraintTasks)
                            .onSuccessTask(constraintResults -> {
                                @SuppressWarnings("unchecked")
                                List<Constraint> constraints = (List<Constraint>) (List<?>) constraintResults;
                                // 4. For each fetched Constraint, fetch its Keypoints.
                                List<Task<ConstraintDetailDTO>> constraintDetailTasks = new ArrayList<>();
                                for (Constraint constraint : constraints) {
                                    List<String> keypointIds = constraint.getKeypoints(); // This is a list of Keypoint IDs.
                                    Task<ConstraintDetailDTO> detailTask = repository.getKeypointsByIds(keypointIds)
                                            .onSuccessTask(keypointList -> {
                                                // Build the detailed constraint DTO.
                                                ConstraintDetailDTO detail = new ConstraintDetailDTO();
                                                detail.setConstraintId(constraint.getConstraintId());
                                                detail.setAlignedThreshold(constraint.getAlignedThreshold());
                                                detail.setRestingThreshold(constraint.getRestingThreshold());
                                                detail.setRestingComparator(constraint.getRestingComparator());
                                                detail.setKeypoints(keypointList);
                                                return Tasks.forResult(detail);
                                            });
                                    constraintDetailTasks.add(detailTask);
                                }
                                // Wait for all constraint detail tasks to finish.
                                return Tasks.whenAllSuccess(constraintDetailTasks)
                                        .onSuccessTask(detailResults -> {
                                            @SuppressWarnings("unchecked")
                                            List<ConstraintDetailDTO> constraintDetails =
                                                    (List<ConstraintDetailDTO>) (List<?>) detailResults;
                                            dto.setConstraints(constraintDetails);
                                            // Return the fully built DTO.
                                            return Tasks.forResult(dto);
                                        });
                            });
                });
    }
}
