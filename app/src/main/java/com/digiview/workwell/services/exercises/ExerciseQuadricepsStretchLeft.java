package com.digiview.workwell.services.exercises;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class ExerciseQuadricepsStretchLeft extends Exercise {

    public ExerciseQuadricepsStretchLeft(Integer repetition, Long duration) {
        super(repetition, duration);
    }

    @Override
    public Exercise.ExerciseResult excerciseResult() {
        double[] a = landmarkToArray(landmarks.get(Exercise.LANDMARKS_FLIPPED.LEFT_ANKLE.getId()));
        double[] b = landmarkToArray(landmarks.get(Exercise.LANDMARKS_FLIPPED.LEFT_KNEE.getId()));
        double[] c = landmarkToArray(landmarks.get(Exercise.LANDMARKS_FLIPPED.LEFT_HIP.getId()));
        double[] d = landmarkToArray(landmarks.get(Exercise.LANDMARKS_FLIPPED.LEFT_SHOULDER.getId()));

        Future<Double> angle3D1Future = calculateAngle3DAsync(a, b, c);

        Future<Double> angle3D2Future = calculateAngle3DAsync(b,c,d);

        // vertex at knee
        double angle3D1 = 0;
        // vertex at hip
        double angle3D2 = 0;

        try {
            // Get results (this will block until the results are available)
            angle3D1 = angle3D1Future.get();
            angle3D2 = angle3D2Future.get();
        } catch (InterruptedException | ExecutionException exception) {
            // Handle the exception (either log it or rethrow it)
            exception.printStackTrace();
        }


        double[] angles = {angle3D1, angle3D2};// nonrelevant


        Exercise.STATUS position;

        if (angle3D1 >=160 && angle3D2 >= 120) {
            position = Exercise.STATUS.RESTING;
        } else if (angle3D1 <= 90 && angle3D2 <= 110) {
            position = Exercise.STATUS.ALIGNED;
        } else {
            position = Exercise.STATUS.TRANSITIONING;
        }
        // Handle state transitions
        switch (position) {
            case RESTING:
                // mark new timer as current
                isTimerReset = false;
                relaxedCount++;
                isRepFinished = false;
                stretchedCount = 0;
                pauseTimer(); // Pause the timer
                if (relaxedCount >= stateThreshold && lastStatus != STATUS.RESTING) {
                    lastStatus = STATUS.RESTING;
                }
                break;

            case ALIGNED:
                stretchedCount++;
                relaxedCount = 0;
                if (stretchedCount >= stateThreshold && lastStatus != STATUS.ALIGNED) {
                    resumeTimer(); // Resume the timer
                    lastStatus = STATUS.ALIGNED;
                }
                break;

            case TRANSITIONING:
                relaxedCount = 0;
                stretchedCount = 0;
                pauseTimer(); // Pause the timer
                lastStatus = STATUS.TRANSITIONING;
                break;
        }




        // Check if current rep is Finished
        STATUS repCheck = (isRepFinished) ? STATUS.REP_FINISHED : position;

        // Check if the counter has reached the target value
        STATUS finalStatus = (counter >= repetition) ? STATUS.FINISHED : repCheck;

        return new ExerciseResult(angles, finalStatus, lastStatus, counter, timeLeft);
    }
}
