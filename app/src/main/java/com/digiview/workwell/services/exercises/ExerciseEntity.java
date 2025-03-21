package com.digiview.workwell.services.exercises;

public class ExerciseEntity {
    private String exerciseGroup;
    private String exerciseName;
    private String exerciseCategory;
    private int repetition;
    private Long duration;
    public ExerciseEntity() {}
    public ExerciseEntity(String exerciseGroup, String exerciseName, String exerciseCategory) {
        this.exerciseGroup = exerciseGroup;
        this.exerciseName = exerciseName;
        this.exerciseCategory = exerciseCategory;
    }
    public ExerciseEntity(String exerciseGroup, String exerciseName, String exerciseCategory, int repetition) {
        this.exerciseGroup = exerciseGroup;
        this.exerciseName = exerciseName;
        this.exerciseCategory = exerciseCategory;
        this.repetition = repetition;
    }
    public ExerciseEntity(String exerciseGroup, String exerciseName, String exerciseCategory, Long duration) {
        this.exerciseGroup = exerciseGroup;
        this.exerciseName = exerciseName;
        this.exerciseCategory = exerciseCategory;
        this.duration = duration;
    }
    public ExerciseEntity(String exerciseGroup, String exerciseName, String exerciseCategory, int repetition, Long duration) {
        this.exerciseGroup = exerciseGroup;
        this.exerciseName = exerciseName;
        this.exerciseCategory = exerciseCategory;
        this.repetition = repetition;
        this.duration = duration;
    }

    public String getExerciseGroup() {
        return exerciseGroup;
    }

    public  String getExerciseName() {
        return  exerciseName;
    }

    public String getExerciseCategory() {
        return exerciseCategory;
    }
    public int getRepetition() {
        return repetition;
    }
    public long getDuration() {
        return duration;
    }
}

