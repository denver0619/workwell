package com.digiview.workwell.services.exercises;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
//NOT UPDATED DO NOT USE
public class ExerciseRectusFemorisStretchLeft extends Exercise{
    public ExerciseRectusFemorisStretchLeft(Integer repetition, Long duration) {
        super(repetition, duration);
    }

    @Override
    public Exercise.ExerciseResult excerciseResult() {
        double[] a = landmarkToArray(landmarks.get(LANDMARKS_FLIPPED.LEFT_ANKLE.getId()));
        double[] b = landmarkToArray(landmarks.get(LANDMARKS_FLIPPED.LEFT_KNEE.getId()));
        double[] c = landmarkToArray(landmarks.get(LANDMARKS_FLIPPED.LEFT_HIP.getId()));
        double[] d = landmarkToArray(landmarks.get(LANDMARKS_FLIPPED.LEFT_SHOULDER.getId()));
        double[] e = landmarkToArray(landmarks.get(LANDMARKS_FLIPPED.RIGHT_ANKLE.getId()));
        double[] f = landmarkToArray(landmarks.get(LANDMARKS_FLIPPED.RIGHT_KNEE.getId()));
        double[] g = landmarkToArray(landmarks.get(LANDMARKS_FLIPPED.RIGHT_HIP.getId()));

        Future<Double> leftKneeFuture = calculateAngle3DAsync(a, b, c);

        Future<Double> rightKneeFuture = calculateAngle3DAsync(e,f,g);

        Future<Double> leftHipFuture = calculateAngle3DAsync(b,c,d);

        // vertex at left knee
        double leftKnee = 0;
        // vertex at right knee
        double rightKnee = 0;
        // vertex at left hip
        double leftHip = 0;

        try {
            // Get results (this will block until the results are available)
            leftKnee = leftKneeFuture.get();
            rightKnee = rightKneeFuture.get();
            leftHip = leftHipFuture.get();
        } catch (InterruptedException | ExecutionException exception) {
            // Handle the exception (either log it or rethrow it)
            exception.printStackTrace();
        }


        double[] angles = {leftKnee, rightKnee};// nonrelevant

        Exercise.STATUS position;

        if (leftHip >= 150 && 65 <= rightKnee && rightKnee <= 120 && leftKnee>=60 ) {
            position = Exercise.STATUS.RESTING;
        } else if (leftHip >= 160 && 65 <= rightKnee && rightKnee <= 120 && leftKnee<=50 ) {
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




        // Check if the counter has reached the target value
        STATUS finalStatus = (counter >= repetition) ? STATUS.FINISHED : position;

        return new ExerciseResult(angles, finalStatus, lastStatus, counter, timeLeft);
    }
}
