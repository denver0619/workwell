package com.digiview.workwell.data.models;

import java.io.Serializable;

public class RoutineExercise implements Serializable{
    private String ExerciseId;
    private int Reps;
    private int Duration;
    private String ExerciseName;        // New field
    private String ExerciseDescription; // New field
    private String VideoId;
    // Getters and Setters
    public String getExerciseId() {
        return ExerciseId;
    }

    public void setExerciseId(String exerciseId) {
        ExerciseId = exerciseId;
    }

    public int getReps() {
        return Reps;
    }

    public void setReps(int reps) {
        Reps = reps;
    }

    public long getDuration() {
        return Duration;
    }

    public void setDuration(int duration) {
        Duration = duration;
    }

    public String getExerciseName() {
        return ExerciseName;
    }

    public void setExerciseName(String exerciseName) {
        ExerciseName = exerciseName;
    }

    public String getExerciseDescription() {
        return ExerciseDescription;
    }

    public void setExerciseDescription(String exerciseDescription) {
        ExerciseDescription = exerciseDescription;
    }

    public String getVideoId() {
        return VideoId;
    }

    public void setVideoId(String videoId) {
        VideoId = videoId;
    }

    @Override
    public String toString() {
        return "RoutineExercise{" +
                "ExerciseId='" + ExerciseId + '\'' +
                ", Reps=" + Reps +
                ", Sets=" + Duration +
                ", Video=" + VideoId +
                ", ExerciseName='" + ExerciseName + '\'' +
                ", ExerciseDescription='" + ExerciseDescription + '\'' +
                '}';
    }
}

