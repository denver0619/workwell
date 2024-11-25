package com.digiview.workwell.services.exercises;

public class ExerciseFactory {
    public Exercise createExercise(String name, Integer repetition, Long duration) {
        Exercise exercise;
        switch (name) {
            case "Longus Colli Stretch":
                exercise = new ExerciseLongusColliStretch(repetition, duration);
                break;
            case "Quadriceps Stretch Right":
                exercise = new ExerciseQuadricepsStretchRight(repetition, duration);
                break;
            case "Quadriceps Stretch Left":
                exercise = new ExerciseQuadricepsStretchLeft(repetition, duration);
                break;
            case "Rectus Femoris Stretch Left":
                exercise = new ExerciseRectusFemorisStretchLeft(repetition, duration);
                break;
            case "Wrist Extensor Stretch Left":
                exercise = new ExerciseWristExtensorStretchLeft(repetition, duration);
                break;
            case "Latissimus Dorsi, Teres Major Stretch Left":
                exercise = new ExerciseLatissimusDorsiTeresMajorStretchLeft(repetition,duration);
                break;
            case "Latissimus Dorsi, Teres Major Stretch Right":
                exercise = new ExerciseLatissimusDorsiTeresMajorStretchRight(repetition, duration);
                break;
            default:
                return null;
        }
        return  exercise;
    }

}
