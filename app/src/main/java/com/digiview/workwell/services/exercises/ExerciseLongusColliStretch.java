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
        double[] a = landmarkToArray(landmarks.get(LANDMARKS.RIGHT_SHOULDER.getId()));
        double[] b = landmarkToArray(landmarks.get(LANDMARKS.LEFT_SHOULDER.getId()));
        double[] c = landmarkToArray(landmarks.get(LANDMARKS.NOSE.getId()));
        double[] d = landmarkToArray(landmarks.get(LANDMARKS.RIGHT_EAR.getId()));
        double[] e = landmarkToArray(landmarks.get(LANDMARKS.LEFT_EAR.getId()));
//        double[] d = landmarkToArray(landmarks.get(LANDMARKS.RIGHT_EYE.getId()));
//        double[] e = landmarkToArray(landmarks.get(LANDMARKS.LEFT_EYE.getId()));
//        double[] f = landmarkToArray(landmarks.get(LANDMARKS.MOUTH_RIGHT.getId()));
//        double[] g = landmarkToArray(landmarks.get(LANDMARKS.MOUTH_LEFT.getId()));
        double[] f = landmarkToArray(landmarks.get(LANDMARKS.RIGHT_EYE.getId()));
        double[] g = landmarkToArray(landmarks.get(LANDMARKS.LEFT_EYE.getId()));



        // Run 3D calculation in a separate thread
        double[] abMid = calculateMidpoint3D(a,b);
        double[] deMid = calculateMidpoint3D(d,e);
        double[] fgMid = calculateMidpoint3D(f,g);
        Future<Double> angle3DFuture = calculateAngle3DAsync(abMid, deMid, fgMid);

        double angle3D = 0;

        try {
            // Get results (this will block until the results are available)
            angle3D = angle3DFuture.get();
        } catch (InterruptedException | ExecutionException err) {
            // Handle the exception (either log it or rethrow it)
            err.printStackTrace();
        }
//        finally {
//            // Shut down the executor service after use
//            executor.shutdown();
//        }

        double[] angles = {angle3D , angle3D};

        //TODO: Angle Threshold Test
        STATUS position;
        if (angle3D >= 140) {
            position = STATUS.RESTING;
        } else if (angle3D <= 135) {
            position = STATUS.ALIGNED;
        } else {
            position = STATUS.TRANSITIONING;
        }
//        position = STATUS.RESTING;//TODO: Remove after debug
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
