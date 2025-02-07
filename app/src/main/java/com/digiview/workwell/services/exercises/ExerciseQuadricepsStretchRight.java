package com.digiview.workwell.services.exercises;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class ExerciseQuadricepsStretchRight extends AbstractExercise {

    public ExerciseQuadricepsStretchRight(Integer repetition, Long duration) {
        super(repetition, duration);
    }

    @Override
    public ExerciseResult excerciseResult()  {
        // Get 3D angles asynchronously
        double[] a = landmarkToArray(landmarks.get(LANDMARKS_FLIPPED.RIGHT_ANKLE.getId()));
        double[] b = landmarkToArray(landmarks.get(LANDMARKS_FLIPPED.RIGHT_KNEE.getId()));
        double[] c = landmarkToArray(landmarks.get(LANDMARKS_FLIPPED.RIGHT_HIP.getId()));
        double[] d = landmarkToArray(landmarks.get(LANDMARKS_FLIPPED.RIGHT_SHOULDER.getId()));

        Future<Double> angle3D1Future = calculateAngle3DAsync(a, b, c);
        Future<Double> angle3D2Future = calculateAngle3DAsync(b, c, d);

        double angle3D1 = 0;
        double angle3D2 = 0;

        try {
            angle3D1 = angle3D1Future.get();
            angle3D2 = angle3D2Future.get();
        } catch (InterruptedException | ExecutionException exception) {
            exception.printStackTrace();
        }

        double[] angles = {angle3D1, angle3D2};

        // Determine position
        STATUS position;
        if (angle3D1 >= 140 && angle3D2 >= 100) {
            position = STATUS.RESTING;
        } else if (angle3D1 <= 130 && angle3D2 >= 100) {
            position = STATUS.ALIGNED;
        } else {
            position = STATUS.TRANSITIONING;
        }

        // Handle state transitions
        switch (position) {
            case RESTING:
                // mark new timer as current
                if (isRepFinished){
                    isRepFinished = false;
                    restartTimer();
                }
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



        // Check if current rep is Finished
        STATUS repCheck = (isRepFinished) ? STATUS.REP_FINISHED : position;

        // Check if the counter has reached the target value
        STATUS finalStatus = (counter >= repetition) ? STATUS.FINISHED : repCheck;

        return new ExerciseResult(angles, finalStatus, lastStatus, counter, timeLeft);
    }


}
