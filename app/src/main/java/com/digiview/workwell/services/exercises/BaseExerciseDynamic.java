package com.digiview.workwell.services.exercises;

import com.digiview.workwell.data.models.Keypoint;
import com.digiview.workwell.data.models.RoutineExerciseDetailDTO;

import org.checkerframework.checker.units.qual.C;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class BaseExerciseDynamic extends AbstractExercise {
    private final RoutineExerciseDetailDTO routineExerciseDetailDTO;
    public boolean restingState;
    public boolean alignedState;
    public BaseExerciseDynamic(RoutineExerciseDetailDTO routineExerciseDetailDTO) {
        super(routineExerciseDetailDTO.getReps(), routineExerciseDetailDTO.getDuration());
        this.routineExerciseDetailDTO = routineExerciseDetailDTO;
    }

//    // DATABASE ENTITIES
//    public static class KeyPoint {
//        // required, non-null
//        public Boolean isMidpoint;
//        // required, non-null
//        public Integer pointA; // index in 33 Keypoint
//        // optional, nullable (required only when isMidpoint is true)
//        public Integer pointB; // index in 33 Kypoint
//    }
//
//    public static class Constraint {
//        public List<KeyPoint> keyPoints; //required, non-null (fixed 3 Keypoint)
//        public Float restingAngleThreshold; // required, non-null
//        public Float alignedAngleThreshold; // required, non-null
//
//        /**
//         * Only two option
//         * "lt" for less than
//         * "gt" for greater than
//         * NOTE: should put warning or prevent the doctor if inputting invalid states (for combination of thresholds and comparator)
//         */
//        public String restingAngleComparator; // required, non-null
//        public String alignedAngleComparator;//required, non-null
//    }
//
//    public static class Exercise {
//        public String name;
//        public String description;
//        public List<Constraint> constraints;
//    }
//    // DATABASE ENTITIES

    private double[] keyPointToCoordinateConversion(Keypoint keyPoint) {

        double[] result;

        if(keyPoint.isMidpoint()) {
            result = calculateMidpoint3D(
                    landmarkToArray(landmarks.get(LANDMARKS_FLIPPED.fromString(keyPoint.getKeypoint()).getId())),
                    landmarkToArray(landmarks.get(LANDMARKS_FLIPPED.fromString(keyPoint.getSecondaryKeypoint()).getId()))
            );
        } else {
            result = landmarkToArray(landmarks.get(LANDMARKS_FLIPPED.fromString(keyPoint.getKeypoint()).getId()));
        }
        return result;
    }

    @Override
    public ExerciseResult excerciseResult() {
        /**TODO: Loop through list of constraints and use && operator for resting and aligned
         * if resting = true and aligned = false, position resting
         * if resting = false and aligned = true, position aligned
         * if both are false, position transition
         * if both are true, position invalid
         */

        List<Double> angleResults = new ArrayList<>();
        Future<Double> angle3DFuture;
        boolean resting = true;
        boolean aligned = true;


        for (RoutineExerciseDetailDTO.ConstraintDetailDTO constraint : routineExerciseDetailDTO.getConstraints()) {
            List<Keypoint> keyPoints = constraint.getKeypoints();
            double[] pointA = keyPointToCoordinateConversion(keyPoints.get(0));
            double[] pointB = keyPointToCoordinateConversion(keyPoints.get(1));
            double[] pointC = keyPointToCoordinateConversion(keyPoints.get(2));

            angle3DFuture = calculateAngle3DAsync(pointA, pointB, pointC);
            double angle3d = 0;
            try {
                // Get results (this will block until the results are available)
                angle3d = angle3DFuture.get();
                angleResults.add(angle3d);
                angleResults.add(angle3d);
            } catch (InterruptedException | ExecutionException err) {
                // Handle the exception (either log it or rethrow it)
                err.printStackTrace();
            }

            angleResults.add(angle3d);
//            // if "lt" the status will be resting if less than the resting threshold and aligned if greater than the aligned threshold
//            // if "gt" the status will be resting of greater than the resting threshold and aligned if less than the aligned threshold
//            if (constraint.restingAngleComparator.equals("lt")) {
//                if (angle3d < constraint.restingAngleThreshold){
//                    resting = resting && true;
//                } else {
//                    resting = resting && false;
//                }
//
//                if (angle3d > constraint.alignedAngleThreshold) {
//                    aligned = aligned && true;
//                } else {
//                    aligned = aligned && false;
//                }
//            } else if (constraint.restingAngleComparator.equals("gt")) {
//                if (angle3d > constraint.restingAngleThreshold){
//                    resting = resting && true;
//                } else {
//                    resting = resting && false;
//                }
//
//                if (angle3d < constraint.alignedAngleThreshold) {
//                    aligned = aligned && true;
//                } else {
//                    aligned = aligned && false;
//                }
//
//            }
            //=================================================
//            if (constraint.getRestingComparator().equals("lt") && constraint.getAlignedComparator().equals("lt")) {
//                if (angle3d < constraint.getRestingThreshold()) {
//                    resting = resting && true;
//                } else {
//                    resting = resting && false;
//                }
//
//                if (angle3d < constraint.getAlignedThreshold()) {
//                    aligned = aligned && true;
//                } else {
//                    aligned = aligned && false;
//                }
//            } else if (constraint.getRestingComparator().equals("gt") && constraint.getAlignedComparator().equals("gt")) {
//                if (angle3d > constraint.getRestingThreshold()) {
//                    resting = resting && true;
//                } else {
//                    resting = resting && false;
//                }
//
//                if (angle3d > constraint.getAlignedThreshold()) {
//                    aligned = aligned && true;
//                } else {
//                    aligned = aligned && false;
//                }
//            } else if (constraint.getRestingComparator().equals("lt") && constraint.getAlignedComparator().equals("gt")) {
//                if (angle3d < constraint.getRestingThreshold()) {
//                    resting = resting && true;
//                } else {
//                    resting = resting && false;
//                }
//
//                if (angle3d > constraint.getAlignedThreshold()) {
//                    aligned = aligned && true;
//                } else {
//                    aligned = aligned && false;
//                }
//            } else if (constraint.getRestingComparator().equals("gt") && constraint.getAlignedComparator().equals("lt")) {
//                if (angle3d > constraint.getRestingThreshold()) {
//                    resting = resting && true;
//                } else {
//                    resting = resting && false;
//                }
//
//                if (angle3d < constraint.getAlignedThreshold()) {
//                    aligned = aligned && true;
//                } else {
//                    aligned = aligned && false;
//                }
//            }
            //====================
//            if (constraint.getRestingComparator().equals("lt")) {
//                resting = resting && (angle3d < constraint.getRestingThreshold());
//            } else if (constraint.getRestingComparator().equals("gt")) {
//                resting = resting && (angle3d > constraint.getRestingThreshold());
//            }
//
//            if (constraint.getAlignedComparator().equals("lt")) {
//                aligned = aligned && (angle3d < constraint.getAlignedThreshold());
//            } else if (constraint.getAlignedComparator().equals("gt")) {
//                aligned = aligned && (angle3d > constraint.getAlignedThreshold());
//            }
            //===================
            if (constraint.getRestingComparator().equals("lt")) {
                resting = resting && (angle3d < constraint.getRestingThreshold());
            } else if (constraint.getRestingComparator().equals("gt")) {
                resting = resting && (angle3d > constraint.getRestingThreshold());
            }

            if (constraint.getAlignedComparator().equals("lt")) {
                aligned = aligned && (angle3d < constraint.getAlignedThreshold());
            } else if (constraint.getAlignedComparator().equals("gt")) {
                aligned = aligned && (angle3d > constraint.getAlignedThreshold());
            }
        }

        STATUS position;
        if (resting && !aligned) {
            position = STATUS.RESTING;
        } else  if (!resting && aligned) {
            position = STATUS.ALIGNED;
        } else if (!resting && !aligned) {
            position = STATUS.TRANSITIONING;
        } else {
            position = STATUS.NO_PERSON;
        }
        restingState = resting;
        alignedState = aligned;
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



        double[] angles = angleResults.stream().mapToDouble(Double::doubleValue).toArray();
        // Check if current rep is Finished
        STATUS repCheck = (isRepFinished) ? STATUS.REP_FINISHED : position;

        // Check if the counter has reached the target value
        // This check to keep ringing on last rep when still aligned
        STATUS finalStatus = (counter >= repetition) && (!position.equals(STATUS.ALIGNED)) ? STATUS.FINISHED : repCheck;

        return new ExerciseResult(angles, finalStatus, lastStatus, counter, timeLeft);
    }

}
