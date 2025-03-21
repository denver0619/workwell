package com.digiview.workwell.ui.routine.execution;

public class RoutineExerciseEntity {
    public RoutineExerciseEntity() {}
    public RoutineExerciseEntity(
            String ExerciseId,
            String ExerciseName,
            String ExerciseDescription,
            Integer Repetition,
            Long Duration
    ) {
        this.ExerciseID = ExerciseId;
        this.ExerciseName = ExerciseName;
        this.ExerciseDescription = ExerciseDescription;
        this.Repetition = Repetition;
        this.Duration = Duration;
    }

    public String ExerciseID;
    public String ExerciseName;
    public String ExerciseDescription;
    public Integer Repetition;
    public Long Duration;
}
