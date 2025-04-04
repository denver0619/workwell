package com.digiview.workwell.services.exercises;

import android.os.CountDownTimer;

import com.google.mediapipe.tasks.components.containers.NormalizedLandmark;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.IntStream;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractExercise {
    protected final ExecutorService executor = Executors.newFixedThreadPool(2);
    // class variables
    protected List<NormalizedLandmark> landmarks;
    protected int repetition;
    protected long duration;
    protected int counter = 0;
    protected long timeLeft;
    protected Long defaultTick = 10L;
    protected CountDownTimer countDownTimer;
    protected boolean isTimerRunning = false; // Track timer state
    protected boolean isRepFinished = false; // check if current rep is finished

    // FOR COUNTING REPS

    protected int relaxedCount = 0;
    protected int stretchedCount = 0;
    protected int stateThreshold = 3; // Example threshold, define as needed
    protected STATUS lastStatus;
    protected boolean isFromRelaxed = false;
    protected boolean isTimerReset = true;

    public int getRepetition() {
        return repetition;
    }
    public long getDuration() {
        return timeLeft;
    }

    // landmark index
//    public enum LANDMARKS {
//        NOSE(0),
//        LEFT_EYE_INNER(1),
//        LEFT_EYE(2),
//        LEFT_EYE_OUTER(3),
//        RIGHT_EYE_INNER(4),
//        RIGHT_EYE(5),
//        RIGHT_EYE_OUTER(6),
//        LEFT_EAR(7),
//        RIGHT_EAR(8),
//        MOUTH_LEFT(9),
//        MOUTH_RIGHT(10),
//        LEFT_SHOULDER(11),
//        RIGHT_SHOULDER(12),
//        LEFT_ELBOW(13),
//        RIGHT_ELBOW(14),
//        LEFT_WRIST(15),
//        RIGHT_WRIST(16),
//        LEFT_PINKY(17),
//        RIGHT_PINKY(18),
//        LEFT_INDEX(19),
//        RIGHT_INDEX(20),
//        LEFT_THUMB(21),
//        RIGHT_THUMB(22),
//        LEFT_HIP(23),
//        RIGHT_HIP(24),
//        LEFT_KNEE(25),
//        RIGHT_KNEE(26),
//        LEFT_ANKLE(27),
//        RIGHT_ANKLE(28),
//        LEFT_HEEL(29),
//        RIGHT_HEEL(30),
//        LEFT_FOOT_INDEX(31),
//        RIGHT_FOOT_INDEX(32);
//
//        private int id;
//        LANDMARKS(int id) {
//            this.id = id;
//        }
//
//        public int getId() {
//            return id;
//        }
//    }

    public enum LANDMARKS {
        NOSE(0, "NOSE"),
        LEFT_EYE_INNER(1, "LEFT_EYE_INNER"),
        LEFT_EYE(2, "LEFT_EYE"),
        LEFT_EYE_OUTER(3, "LEFT_EYE_OUTER"),
        RIGHT_EYE_INNER(4, "RIGHT_EYE_INNER"),
        RIGHT_EYE(5, "RIGHT_EYE"),
        RIGHT_EYE_OUTER(6, "RIGHT_EYE_OUTER"),
        LEFT_EAR(7, "LEFT_EAR"),
        RIGHT_EAR(8, "RIGHT_EAR"),
        MOUTH_LEFT(9, "MOUTH_LEFT"),
        MOUTH_RIGHT(10, "MOUTH_RIGHT"),
        LEFT_SHOULDER(11, "LEFT_SHOULDER"),
        RIGHT_SHOULDER(12, "RIGHT_SHOULDER"),
        LEFT_ELBOW(13, "LEFT_ELBOW"),
        RIGHT_ELBOW(14, "RIGHT_ELBOW"),
        LEFT_WRIST(15, "LEFT_WRIST"),
        RIGHT_WRIST(16, "RIGHT_WRIST"),
        LEFT_PINKY(17, "LEFT_PINKY"),
        RIGHT_PINKY(18, "RIGHT_PINKY"),
        LEFT_INDEX(19, "LEFT_INDEX"),
        RIGHT_INDEX(20, "RIGHT_INDEX"),
        LEFT_THUMB(21, "LEFT_THUMB"),
        RIGHT_THUMB(22, "RIGHT_THUMB"),
        LEFT_HIP(23, "LEFT_HIP"),
        RIGHT_HIP(24, "RIGHT_HIP"),
        LEFT_KNEE(25, "LEFT_KNEE"),
        RIGHT_KNEE(26, "RIGHT_KNEE"),
        LEFT_ANKLE(27, "LEFT_ANKLE"),
        RIGHT_ANKLE(28, "RIGHT_ANKLE"),
        LEFT_HEEL(29, "LEFT_HEEL"),
        RIGHT_HEEL(30, "RIGHT_HEEL"),
        LEFT_FOOT_INDEX(31, "LEFT_FOOT_INDEX"),
        RIGHT_FOOT_INDEX(32, "RIGHT_FOOT_INDEX");

        private static final Map<String, LANDMARKS> NAME_MAP = new HashMap<>();

        static {
            for (LANDMARKS landmark : values()) {
                NAME_MAP.put(landmark.name, landmark);
            }
        }

        private final int id;
        private final String name;

        LANDMARKS(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public String getStr() {
            return name;
        }

        // Factory Method
        public static LANDMARKS fromString(String name) {
            return NAME_MAP.getOrDefault(name, null);
        }
    }
//
//    public enum LANDMARKS_FLIPPED {
//        NOSE(LANDMARKS.NOSE.getId()),
//        LEFT_EYE_INNER(LANDMARKS.RIGHT_EYE_INNER.getId()),
//        LEFT_EYE(LANDMARKS.RIGHT_EYE.getId()),
//        LEFT_EYE_OUTER(LANDMARKS.RIGHT_EYE_OUTER.getId()),
//        RIGHT_EYE_INNER(LANDMARKS.LEFT_EYE_INNER.getId()),
//        RIGHT_EYE(LANDMARKS.LEFT_EYE.getId()),
//        RIGHT_EYE_OUTER(LANDMARKS.LEFT_EYE_OUTER.getId()),
//        LEFT_EAR(LANDMARKS.RIGHT_EAR.getId()),
//        RIGHT_EAR(LANDMARKS.LEFT_EAR.getId()),
//        MOUTH_LEFT(LANDMARKS.MOUTH_RIGHT.getId()),
//        MOUTH_RIGHT(LANDMARKS.MOUTH_LEFT.getId()),
//        LEFT_SHOULDER(LANDMARKS.RIGHT_SHOULDER.getId()),
//        RIGHT_SHOULDER(LANDMARKS.LEFT_SHOULDER.getId()),
//        LEFT_ELBOW(LANDMARKS.RIGHT_ELBOW.getId()),
//        RIGHT_ELBOW(LANDMARKS.LEFT_ELBOW.getId()),
//        LEFT_WRIST(LANDMARKS.RIGHT_WRIST.getId()),
//        RIGHT_WRIST(LANDMARKS.LEFT_WRIST.getId()),
//        LEFT_PINKY(LANDMARKS.RIGHT_PINKY.getId()),
//        RIGHT_PINKY(LANDMARKS.LEFT_PINKY.getId()),
//        LEFT_INDEX(LANDMARKS.RIGHT_INDEX.getId()),
//        RIGHT_INDEX(LANDMARKS.LEFT_INDEX.getId()),
//        LEFT_THUMB(LANDMARKS.RIGHT_THUMB.getId()),
//        RIGHT_THUMB(LANDMARKS.LEFT_THUMB.getId()),
//        LEFT_HIP(LANDMARKS.RIGHT_HIP.getId()),
//        RIGHT_HIP(LANDMARKS.LEFT_HIP.getId()),
//        LEFT_KNEE(LANDMARKS.RIGHT_KNEE.getId()),
//        RIGHT_KNEE(LANDMARKS.LEFT_KNEE.getId()),
//        LEFT_ANKLE(LANDMARKS.RIGHT_ANKLE.getId()),
//        RIGHT_ANKLE(LANDMARKS.LEFT_ANKLE.getId()),
//        LEFT_HEEL(LANDMARKS.RIGHT_HEEL.getId()),
//        RIGHT_HEEL(LANDMARKS.LEFT_HEEL.getId()),
//        LEFT_FOOT_INDEX(LANDMARKS.RIGHT_FOOT_INDEX.getId()),
//        RIGHT_FOOT_INDEX(LANDMARKS.LEFT_FOOT_INDEX.getId());
//
//        private int id;
//        LANDMARKS_FLIPPED(int id) {
//            this.id = id;
//        }
//
//        public int getId() {
//            return id;
//        }
//    }

    public enum LANDMARKS_FLIPPED {
        NOSE(LANDMARKS.NOSE.getId(), "NOSE"),
        LEFT_EYE_INNER(LANDMARKS.RIGHT_EYE_INNER.getId(), "LEFT_EYE_INNER"),
        LEFT_EYE(LANDMARKS.RIGHT_EYE.getId(), "LEFT_EYE"),
        LEFT_EYE_OUTER(LANDMARKS.RIGHT_EYE_OUTER.getId(), "LEFT_EYE_OUTER"),
        RIGHT_EYE_INNER(LANDMARKS.LEFT_EYE_INNER.getId(), "RIGHT_EYE_INNER"),
        RIGHT_EYE(LANDMARKS.LEFT_EYE.getId(), "RIGHT_EYE"),
        RIGHT_EYE_OUTER(LANDMARKS.LEFT_EYE_OUTER.getId(), "RIGHT_EYE_OUTER"),
        LEFT_EAR(LANDMARKS.RIGHT_EAR.getId(), "LEFT_EAR"),
        RIGHT_EAR(LANDMARKS.LEFT_EAR.getId(), "RIGHT_EAR"),
        MOUTH_LEFT(LANDMARKS.MOUTH_RIGHT.getId(), "MOUTH_LEFT"),
        MOUTH_RIGHT(LANDMARKS.MOUTH_LEFT.getId(), "MOUTH_RIGHT"),
        LEFT_SHOULDER(LANDMARKS.RIGHT_SHOULDER.getId(), "LEFT_SHOULDER"),
        RIGHT_SHOULDER(LANDMARKS.LEFT_SHOULDER.getId(), "RIGHT_SHOULDER"),
        LEFT_ELBOW(LANDMARKS.RIGHT_ELBOW.getId(), "LEFT_ELBOW"),
        RIGHT_ELBOW(LANDMARKS.LEFT_ELBOW.getId(), "RIGHT_ELBOW"),
        LEFT_WRIST(LANDMARKS.RIGHT_WRIST.getId(), "LEFT_WRIST"),
        RIGHT_WRIST(LANDMARKS.LEFT_WRIST.getId(), "RIGHT_WRIST"),
        LEFT_PINKY(LANDMARKS.RIGHT_PINKY.getId(), "LEFT_PINKY"),
        RIGHT_PINKY(LANDMARKS.LEFT_PINKY.getId(), "RIGHT_PINKY"),
        LEFT_INDEX(LANDMARKS.RIGHT_INDEX.getId(), "LEFT_INDEX"),
        RIGHT_INDEX(LANDMARKS.LEFT_INDEX.getId(), "RIGHT_INDEX"),
        LEFT_THUMB(LANDMARKS.RIGHT_THUMB.getId(), "LEFT_THUMB"),
        RIGHT_THUMB(LANDMARKS.LEFT_THUMB.getId(), "RIGHT_THUMB"),
        LEFT_HIP(LANDMARKS.RIGHT_HIP.getId(), "LEFT_HIP"),
        RIGHT_HIP(LANDMARKS.LEFT_HIP.getId(), "RIGHT_HIP"),
        LEFT_KNEE(LANDMARKS.RIGHT_KNEE.getId(), "LEFT_KNEE"),
        RIGHT_KNEE(LANDMARKS.LEFT_KNEE.getId(), "RIGHT_KNEE"),
        LEFT_ANKLE(LANDMARKS.RIGHT_ANKLE.getId(), "LEFT_ANKLE"),
        RIGHT_ANKLE(LANDMARKS.LEFT_ANKLE.getId(), "RIGHT_ANKLE"),
        LEFT_HEEL(LANDMARKS.RIGHT_HEEL.getId(), "LEFT_HEEL"),
        RIGHT_HEEL(LANDMARKS.LEFT_HEEL.getId(), "RIGHT_HEEL"),
        LEFT_FOOT_INDEX(LANDMARKS.RIGHT_FOOT_INDEX.getId(), "LEFT_FOOT_INDEX"),
        RIGHT_FOOT_INDEX(LANDMARKS.LEFT_FOOT_INDEX.getId(), "RIGHT_FOOT_INDEX");

        private static final Map<String, LANDMARKS_FLIPPED> NAME_MAP = new HashMap<>();

        static {
            for (LANDMARKS_FLIPPED landmark : values()) {
                NAME_MAP.put(landmark.name, landmark);
            }
        }

        private final int id;
        private final String name;

        LANDMARKS_FLIPPED(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public String getStr() {
            return name;
        }


        public static LANDMARKS_FLIPPED fromString(String name) {
            return NAME_MAP.getOrDefault(name, null);
        }
    }


    // landmark status
    public enum STATUS {
        RESTING,
        TRANSITIONING,
        ALIGNED,
        REP_FINISHED,
        NO_PERSON,
        FINISHED;

    }

    // Result object
    public class ExerciseResult {
        private final double[] angles;
        private final STATUS position;
        private final STATUS lastPosition;
        private final Long timeLeft;
        private final int totalRepetition;

        // ExerciseRepetitionDuration
        public  ExerciseResult(double[] angles, STATUS position, STATUS lastPosition, int totalRepetition, Long timeLeft) {
            this.angles = angles;
            this.position = position;
            this.lastPosition = lastPosition;
            this.totalRepetition = totalRepetition;
            this.timeLeft = timeLeft;
        }

        public double[] getAngles() {
            return angles;
        }

        public STATUS getPosition() {
            return position;
        }
        public STATUS getLastPosition() {return  lastPosition;}

        public int getTotalRepetition() {
            return totalRepetition;
        }
        public Long getTimeLeft() {
            return timeLeft;
        }
    }

    //CONSTRUCTORS
    public AbstractExercise() {}

    public AbstractExercise(int repetition, Long duration) {
        this.repetition = repetition;
        this.duration = duration;
    }

    public AbstractExercise setLandmarkerResult(List<NormalizedLandmark> landmarkerResult) {
        this.landmarks = landmarkerResult;
        return this;
    }

    //CALCULATIONS
    protected double[] calculateMidpoint3D(double[] a, double[] b) {
        return IntStream.range(0, a.length).mapToDouble(i -> (a[i] + b[i])/2).toArray();
    }

    private double calculateAngle2D(double[] a, double[] b, double[] c) {
        //calculate vector ab and bc
        double[] ab = {a[0] - b[0], a[1]-b[1]};
        double[] bc = {c[0] - b[0], c[1]-b[1]};

        //calculate dot product
        double dotProduct = ab[0] * bc[0] + ab[1] * bc[1];

        //calculate magnitudes of ab and bc
        double magnitudeAB = Math.sqrt(ab[0]*ab[0] + ab[1]*ab[1]);
        double magnitudeBC = Math.sqrt(bc[0]*bc[0] + bc[1]*bc[1]);

        //calculate angle in radians
        double radians = Math.acos(dotProduct/(magnitudeAB*magnitudeBC));

        double angle = Math.toDegrees(radians);

        return angle;
    }

    private double calculateAngle3D(double[] a, double[] b, double[] c) {
        //calculate vector ab and bc
        double[] ab = {a[0] - b[0], a[1]-b[1], a[2]-b[2]};
        double[] bc = {c[0] - b[0], c[1]-b[1], c[2]-b[2]};

        //calculate dot product
        double dotProduct = ab[0] * bc[0] + ab[1] * bc[1] +ab[2] * bc[2];

        //calculate magnitudes of ab and bc
        double magnitudeAB = Math.sqrt(ab[0]*ab[0] + ab[1]*ab[1] + ab[2]*ab[2]);
        double magnitudeBC = Math.sqrt(bc[0]*bc[0] + bc[1]*bc[1] + bc[2]*bc[2]);

        //calculate angle in radians
        double radians = Math.acos(dotProduct/(magnitudeAB*magnitudeBC));

        double angle = Math.toDegrees(radians);

        return angle;
    }

    protected Future<Double> calculateAngle2DAsync(double[] a, double[] b, double[] c) {
        return executor.submit(() -> calculateAngle2D(a, b, c));
    }

    protected Future<Double> calculateAngle3DAsync(double[] a, double[] b, double[] c) {
        return executor.submit(() -> calculateAngle3D(a, b, c));
    }

    protected double[] landmarkToArray(NormalizedLandmark normalizedLandmark) {
        return new double[] {normalizedLandmark.x(), normalizedLandmark.y(), normalizedLandmark.z()};
    }

    public void init() {// Initialize the timer
        timeLeft = duration;
        initTimer(duration);
        pauseTimer();

    }
    protected void initTimer(long duration) {
        // Cancel any existing timer before starting a new one
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        timeLeft = duration;
        countDownTimer = new CountDownTimer(timeLeft, defaultTick) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeft = millisUntilFinished;
            }

            @Override
            public void onFinish() {
                timeLeft = 0;
                counter++;
                isTimerReset = true;
                isFromRelaxed = false;
                isTimerRunning = false; // Stop when repetitions are complete
                isRepFinished = true;
                // Restart the timer
//                initTimer(duration);

            }
        };
    }

    protected void startTimer() {
        countDownTimer.start();
        isTimerRunning = true;
    }

    protected void pauseTimer() {
        if (isTimerRunning) {
            countDownTimer.cancel();
            isTimerRunning = false;
        }
    }

    protected void resumeTimer() {
//        if (!isTimerRunning && timeLeft > 0) {
            //continue timer only if the same timer and new rep
            if (!isTimerReset) {
                initTimer(timeLeft);
                startTimer();
            }
//        }
    }

    protected  void restartTimer() {
        if (isTimerReset) {
            countDownTimer.cancel();
            initTimer(duration);
        }
    }

    public abstract ExerciseResult excerciseResult();

}
