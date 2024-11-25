package com.digiview.workwell.services.exercises;

import android.os.CountDownTimer;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class ExerciseLongusColliStretch extends  Exercise {

    public ExerciseLongusColliStretch(Integer repetition, Long duration) {
        super(repetition, duration);
    }
    @Override
    public ExerciseResult excerciseResult() {
        double[] a = landmarkToArray(landmarks.get(LANDMARKS.RIGHT_EAR.getId()));
        double[] b = landmarkToArray(landmarks.get(LANDMARKS.NOSE.getId()));
        double[] c = landmarkToArray(landmarks.get(LANDMARKS.MOUTH_RIGHT.getId()));


        // Run 3D calculation in a separate thread
        Future<Double> angle3DFuture = calculateAngle3DAsync(a, b, c);

        double angle3D = 0;

        try {
            // Get results (this will block until the results are available)
            angle3D = angle3DFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            // Handle the exception (either log it or rethrow it)
            e.printStackTrace();
        }
//        finally {
//            // Shut down the executor service after use
//            executor.shutdown();
//        }

        double[] angles = {angle3D , angle3D};

        STATUS position;
        if (angle3D >=13) {
            position = STATUS.RESTING;
        } else if (angle3D < 12) {
            position = STATUS.ALIGNED;
        } else {
            position = STATUS.TRANSITIONING;
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
