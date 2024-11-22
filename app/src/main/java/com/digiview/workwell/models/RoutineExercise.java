package com.digiview.workwell.models;

public class RoutineExercise {
    private String ExerciseId;
    private int Reps;
    private int Sets;
    private int Rest;
    private String ExerciseName;
    private String ExerciseDescription;

    // Getters and setters
    public String getExerciseId() {
        return ExerciseId;
    }

    public void setExerciseId(String exerciseId) {
        this.ExerciseId = exerciseId;
    }

    public int getReps() {
        return Reps;
    }

    public void setReps(int reps) {
        this.Reps = reps;
    }

    public int getSets() {
        return Sets;
    }

    public void setSets(int sets) {
        this.Sets = sets;
    }

    public int getRest() {
        return Rest;
    }

    public void setRest(int rest) {
        this.Rest = rest;
    }

    public String getExerciseName() {
        return ExerciseName;
    }

    public void setExerciseName(String exerciseName) {
        this.ExerciseName = exerciseName;
    }

    public String getExerciseDescription() {
        return ExerciseDescription;
    }

    public void setExerciseDescription(String exerciseDescription) {
        this.ExerciseDescription = exerciseDescription;
    }

    @Override
    public String toString() {
        return "RoutineExercise{" +
                "ExerciseId='" + ExerciseId + '\'' +
                ", Reps=" + Reps +
                ", Sets=" + Sets +
                ", Rest=" + Rest +
                ", ExerciseName='" + ExerciseName + '\'' +
                ", ExerciseDescription='" + ExerciseDescription + '\'' +
                '}';
    }
}
