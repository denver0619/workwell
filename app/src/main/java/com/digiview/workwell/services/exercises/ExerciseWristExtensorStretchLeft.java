package com.digiview.workwell.services.exercises;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
//NOT UPDATED DO NOT USE
public class ExerciseWristExtensorStretchLeft extends Exercise{
    public ExerciseWristExtensorStretchLeft(Integer repetition, Long duration) {
        super(repetition, duration);
    }

    @Override
    public Exercise.ExerciseResult excerciseResult() {
        double[] a = landmarkToArray(landmarks.get(LANDMARKS_FLIPPED.LEFT_INDEX.getId()));
        double[] b = landmarkToArray(landmarks.get(LANDMARKS_FLIPPED.LEFT_WRIST.getId()));
        double[] c = landmarkToArray(landmarks.get(LANDMARKS_FLIPPED.LEFT_ELBOW.getId()));
        double[] d = landmarkToArray(landmarks.get(LANDMARKS_FLIPPED.LEFT_SHOULDER.getId()));

        Future<Double> leftWristFuture = calculateAngle3DAsync(a, b, c);
        Future<Double> leftElbowFuture = calculateAngle3DAsync(b,c,d);


        // vertex
        double leftWrist = 0;
        double leftElbow = 0;

        try {
            // Get results (this will block until the results are available)
            leftWrist = leftWristFuture.get();
            leftElbow = leftElbowFuture.get();
        } catch (InterruptedException | ExecutionException exception) {
            // Handle the exception (either log it or rethrow it)
            exception.printStackTrace();
        }


        double[] angles = {leftWrist, leftElbow};// nonrelevant

        Exercise.STATUS position;

        if (leftElbow >= 160 && leftWrist >= 160) {
            position = Exercise.STATUS.RESTING;
        } else if (leftElbow >= 160 && leftWrist <= 110) {
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
