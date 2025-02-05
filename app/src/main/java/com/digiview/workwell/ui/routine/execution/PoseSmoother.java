package com.digiview.workwell.ui.routine.execution;

//import com.google.mediapipe.tasks.components.containers.NormalizedLandmark;
//import java.util.LinkedList;
//import java.util.Queue;

//public class PoseSmoother {
//    private final int windowSize;
//    private final Queue<NormalizedLandmark> landmarks;
//    private double sumX, sumY, sumZ;
//
//    public PoseSmoother(int windowSize) {
//        this.windowSize = windowSize;
//        this.landmarks = new LinkedList<>();
//        this.sumX = 0;
//        this.sumY = 0;
//        this.sumZ = 0;
//    }
//
//    public NormalizedLandmark update(NormalizedLandmark newLandmark) {
//        newLandmark.
//        // Add the new landmark to the queue
//        landmarks.add(newLandmark);
//        sumX += newLandmark.getX();
//        sumY += newLandmark.getY();
//        sumZ += newLandmark.getZ();
//
//        // If the queue exceeds the window size, remove the oldest landmark
//        if (landmarks.size() > windowSize) {
//            NormalizedLandmark oldest = landmarks.poll();
//            sumX -= oldest.getX();
//            sumY -= oldest.getY();
//            sumZ -= oldest.getZ();
//        }
//
//        // Calculate the average of the X, Y, Z coordinates
//        double avgX = sumX / landmarks.size();
//        double avgY = sumY / landmarks.size();
//        double avgZ = sumZ / landmarks.size();
//
//        // Return the new smoothed landmark
//
//        return NormalizedLandmark.newBuilder()
//                .setX(avgX)
//                .setY(avgY)
//                .setZ(avgZ)
//                .build();
//    }
//}
import com.google.mediapipe.tasks.components.containers.NormalizedLandmark;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;

public class PoseSmoother {
    private final int windowSize;
    private final Queue<List<NormalizedLandmark>> frames;

    public PoseSmoother(int windowSize) {
        this.windowSize = windowSize;
        this.frames = new LinkedList<>();
    }

    // Update function that smooths the landmarks
    public List<NormalizedLandmark> update(List<NormalizedLandmark> newLandmarks) {
        frames.add(newLandmarks);

        if (frames.size() > windowSize) {
            frames.poll(); // Remove the oldest frame
        }

        // Calculate the smoothed values for each landmark
        return getSmoothedLandmarks();
    }

    // Apply averaging to all landmarks in the list
    private List<NormalizedLandmark> getSmoothedLandmarks() {
        List<NormalizedLandmark> smoothedLandmarks = new LinkedList<>();
        for (int i = 0; i < 33; i++) {
            double sumX = 0;
            double sumY = 0;
            double sumZ = 0;
            int count = 0;

            for (List<NormalizedLandmark> frame : frames) {
                NormalizedLandmark landmark = frame.get(i);
                sumX += landmark.x();
                sumY += landmark.y();
                sumZ += landmark.z(); // Use Z if needed
                count++;
            }

            // Calculate the smoothed coordinates
            double finalSumX = sumX;
            double finalSumY = sumY;
            double finalSumZ = sumZ;
            int finalCount = count;
            smoothedLandmarks.add(new NormalizedLandmark() {
                @Override
                public float x() {
                    return (float) (finalSumX / finalCount);
                }

                @Override
                public float y() {
                    return (float) (finalSumY / finalCount);
                }

                @Override
                public float z() {
                    return (float) (finalSumZ / finalCount); // Use Z if needed
                }

                @Override
                public Optional<Float> visibility() {
                    return Optional.empty();
                }

                @Override
                public Optional<Float> presence() {
                    return Optional.empty();
                }
            });
        }
        return smoothedLandmarks;
    }
}