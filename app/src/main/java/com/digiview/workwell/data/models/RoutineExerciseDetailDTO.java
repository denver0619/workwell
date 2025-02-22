package com.digiview.workwell.data.models;

import com.digiview.workwell.data.models.Exercise;
import com.digiview.workwell.data.models.Keypoint;
import java.io.Serializable;
import java.util.List;

public class RoutineExerciseDetailDTO implements Serializable {

    // RoutineExercise basic fields
    private String exerciseId;         // Could be the routine exercise ID
    private int reps;
    private long duration;
    private String exerciseName;
    private String exerciseDescription;
    private String exerciseDeviceSetup;

    // Detailed exercise info
    private Exercise exercise;

    // Constraint details including keypoints
    private List<ConstraintDetailDTO> constraints;

    // Constructors
    public RoutineExerciseDetailDTO() {
    }

    public RoutineExerciseDetailDTO(String exerciseId, int reps, int duration, String exerciseName,
                                    String exerciseDescription, String exerciseDeviceSetup, Exercise exercise, List<ConstraintDetailDTO> constraints) {
        this.exerciseId = exerciseId;
        this.reps = reps;
        this.duration = duration;
        this.exerciseName = exerciseName;
        this.exerciseDescription = exerciseDescription;
        this.exerciseDeviceSetup = exerciseDeviceSetup;
        this.exercise = exercise;
        this.constraints = constraints;
    }

    // Getters and Setters
    public String getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(String exerciseId) {
        this.exerciseId = exerciseId;
    }

    public int getReps() {
        return reps;
    }

    public void setReps(int reps) {
        this.reps = reps;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getExerciseName() {
        return exerciseName;
    }

    public void setExerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
    }

    public String getExerciseDescription() {
        return exerciseDescription;
    }

    public void setExerciseDescription(String exerciseDescription) {
        this.exerciseDescription = exerciseDescription;
    }

    public String getExerciseDeviceSetup() {
        return exerciseDeviceSetup;
    }

    public void setExerciseDeviceSetup(String exerciseDeviceSetup) {
        this.exerciseDeviceSetup = exerciseDeviceSetup;
    }

    public Exercise getExercise() {
        return exercise;
    }

    public void setExercise(Exercise exercise) {
        this.exercise = exercise;
    }

    public List<ConstraintDetailDTO> getConstraints() {
        return constraints;
    }

    public void setConstraints(List<ConstraintDetailDTO> constraints) {
        this.constraints = constraints;
    }

    @Override
    public String toString() {
        return "RoutineExerciseDetailDTO{" +
                "exerciseId='" + exerciseId + '\'' +
                ", reps=" + reps +
                ", duration=" + duration +
                ", exerciseName='" + exerciseName + '\'' +
                ", exerciseDescription='" + exerciseDescription + '\'' +
                ", exercise=" + exercise +
                ", constraints=" + constraints +
                '}';
    }

    // Inner DTO class for constraint details with keypoints
    public static class ConstraintDetailDTO implements Serializable {
        private String constraintId;
        private int alignedThreshold;
        private int restingThreshold;
        private String restingComparator;
        private String alignedComparator;
        private List<Keypoint> keypoints; // Detailed keypoint info

        public ConstraintDetailDTO() {
        }

        public ConstraintDetailDTO(String constraintId, int alignedThreshold, int restingThreshold,
                                   String restingComparator, String alignedComparator, List<Keypoint> keypoints) {
            this.constraintId = constraintId;
            this.alignedThreshold = alignedThreshold;
            this.restingThreshold = restingThreshold;
            this.restingComparator = restingComparator;
            this.alignedComparator = alignedComparator;
            this.keypoints = keypoints;
        }

        public String getConstraintId() {
            return constraintId;
        }

        public void setConstraintId(String constraintId) {
            this.constraintId = constraintId;
        }

        public int getAlignedThreshold() {
            return alignedThreshold;
        }

        public void setAlignedThreshold(int alignedThreshold) {
            this.alignedThreshold = alignedThreshold;
        }

        public int getRestingThreshold() {
            return restingThreshold;
        }

        public void setRestingThreshold(int restingThreshold) {
            this.restingThreshold = restingThreshold;
        }

        public String getRestingComparator() {
            return restingComparator;
        }

        public void setRestingComparator(String restingComparator) {
            this.restingComparator = restingComparator;
        }

        public String getAlignedComparator() {
            return alignedComparator;
        }

        public void setAlignedComparator(String alignedComparator) {
            this.alignedComparator = alignedComparator;
        }

        public List<Keypoint> getKeypoints() {
            return keypoints;
        }

        public void setKeypoints(List<Keypoint> keypoints) {
            this.keypoints = keypoints;
        }

        @Override
        public String toString() {
            return "ConstraintDetailDTO{" +
                    "constraintId='" + constraintId + '\'' +
                    ", alignedThreshold=" + alignedThreshold +
                    ", restingThreshold=" + restingThreshold +
                    ", restingComparator=" + restingComparator +
                    ", keypoints=" + keypoints +
                    '}';
        }
    }
}

