package com.digiview.workwell.services.exercises;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class ExerciseLatissimusDorsiTeresMajorStretchLeft extends AbstractExercise {
    public ExerciseLatissimusDorsiTeresMajorStretchLeft(Integer repetition, Long duration) {
        super(repetition, duration);
    }
    @Override
    public AbstractExercise.ExerciseResult excerciseResult() {
        double[] a = landmarkToArray(landmarks.get(LANDMARKS_FLIPPED.LEFT_WRIST.getId()));
        double[] b = landmarkToArray(landmarks.get(LANDMARKS_FLIPPED.LEFT_ELBOW.getId()));
        double[] c = landmarkToArray(landmarks.get(LANDMARKS_FLIPPED.LEFT_SHOULDER.getId()));
        double[] d = landmarkToArray(landmarks.get(LANDMARKS_FLIPPED.LEFT_HIP.getId()));
        double[] e = landmarkToArray(landmarks.get(LANDMARKS_FLIPPED.RIGHT_SHOULDER.getId()));
        double[] f = landmarkToArray(landmarks.get(LANDMARKS_FLIPPED.RIGHT_HIP.getId()));
        double[] g = landmarkToArray(landmarks.get(LANDMARKS_FLIPPED.RIGHT_KNEE.getId()));

//        Future<Double> leftElbowFuture = calculateAngle3DAsync(a, b, c);
        Future<Double> leftShoulderFuture = calculateAngle2DAsync(b,c,d);
        Future<Double> rightHipFuture = calculateAngle2DAsync(e,f,g);


        // vertex
//        double leftElbow = 0;
        double leftShoulder = 0;
        double rightHip = 0;


        try {
            // Get results (this will block until the results are available)
//            leftElbow = leftElbowFuture.get();
            leftShoulder = leftShoulderFuture.get();
            rightHip = rightHipFuture.get();
        } catch (InterruptedException | ExecutionException exception) {
            // Handle the exception (either log it or rethrow it)
            exception.printStackTrace();
        }


        double[] angles = {0,leftShoulder, rightHip};// nonrelevant

        AbstractExercise.STATUS position;

        if (leftShoulder >= 120 && rightHip >= 165) {
            position = AbstractExercise.STATUS.RESTING;
        } else if (leftShoulder >= 120 && rightHip <= 155 ) {
            position = AbstractExercise.STATUS.ALIGNED;
        } else {
            position = AbstractExercise.STATUS.TRANSITIONING;
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
//                lastStatus = STATUS.TRANSITIONING;
                break;
        }




        // Check if current rep is Finished
        STATUS repCheck = (isRepFinished) ? STATUS.REP_FINISHED : position;

        // Check if the counter has reached the target value
        STATUS finalStatus = (counter >= repetition) ? STATUS.FINISHED : repCheck;

        return new ExerciseResult(angles, finalStatus, lastStatus, counter, timeLeft);
    }
}
