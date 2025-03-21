package com.digiview.workwell.services.mediapipe;
import com.google.mediapipe.tasks.core.BaseOptions;
import com.google.mediapipe.tasks.core.OutputHandler;
import com.google.mediapipe.tasks.vision.poselandmarker.PoseLandmarker;
public class PoseDetectionHelper {
    // CONSTANT VALUES DECLARATION/INITIALIZATION
    public enum MODEL {
        LITE(0),
        FULL(1),
        HEAVY(3);

        private final int value;

        MODEL(int value) {
            this.value=value;
        }

        public int getValue() {
            return value;
        }
    }

    public enum HARDWARE {
        CPU,
        GPU;
    }

    public enum HELPER_ERROR {
        OTHER_ERROR,
        ERROR;
    }

    public enum RUNNING_MODE {
        IMAGE,
        VIDEO,
        LIVE_STREAM;
    }

    public static final int DEFAULT_NUM_POSES = 1;
    public static final float DEFAULT_MIN_POSE_DETECTION_CONFIDENCE = .5f;
    public static final float DEFAULT_MIN_POSE_PRESENCE_CONFIDENCE = .5f;
    public static final float DEFAULT_MIN_TRACKING_CONFIDENCE = .5f;
    public static final boolean DEFAULT_OUTPUT_SEGMENTATION_MASKS = false;
    public static final OutputHandler.ResultListener DEFAULT_RESULT_CALLBACK = null;

    PoseDetectionHelper() {
        setupPoseLandmarker();
    }
    //TODO: create an override for custom confidence etc.
//    PoseDetectionHelper() {
//
//    }

    private PoseLandmarker poseLandmarker = null;


    public void setupPoseLandmarker() {
        BaseOptions.Builder baseOptionBuilder = BaseOptions.builder();

    }

}
