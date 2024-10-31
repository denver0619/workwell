package com.digiview.workwell.services.mediapipe;
import android.content.Context;

import com.google.mediapipe.tasks.core.BaseOptions;
import com.google.mediapipe.tasks.core.Delegate;
import com.google.mediapipe.tasks.core.OutputHandler;
import com.google.mediapipe.tasks.vision.core.RunningMode;
import com.google.mediapipe.tasks.vision.poselandmarker.PoseLandmarker;
import com.google.mediapipe.tasks.vision.poselandmarker.PoseLandmarkerResult;

import java.util.List;

public class PoseDetectionHelper {
    // CONSTANT VALUES DECLARATION/INITIALIZATION
    public enum MODEL_BUNDLE {
        LITE(0),
        FULL(1),
        HEAVY(3);

        private final int value;

        MODEL_BUNDLE(int value) {
            this.value=value;
        }

        public int getValue() {
            return value;
        }
    }

    public enum DELEGATE_HARDWARE {
        CPU,
        GPU;
    }

    public enum HELPER_ERROR {
        OTHER_ERROR(0),
        GPU_ERROR(1);

        private final int value;

        HELPER_ERROR(int value) {
            this.value = value;
        }

        public int getValue() {
            return  value;
        }

    }

    //INTERFACES
    public interface  LandmarkerListener {
        int otherError = HELPER_ERROR.OTHER_ERROR.getValue();
        void onError(String error);
        void onError(String error, int ErrorCode);
        void onResults(ResultBundle resultBundle);
    }

    //DATA CLASS (ENTITY)

    public class ResultBundle {
        private List<PoseLandmarkerResult> results;
        private Long inferenceTime;
        private int inputImageHeight;
        private int inputImageWidth;
    }


    public static final int DEFAULT_NUM_POSES = 1;
    public static final float DEFAULT_MIN_POSE_DETECTION_CONFIDENCE = .5f;
    public static final float DEFAULT_MIN_POSE_PRESENCE_CONFIDENCE = .5f;
    public static final float DEFAULT_MIN_TRACKING_CONFIDENCE = .5f;
    public static final boolean DEFAULT_OUTPUT_SEGMENTATION_MASKS = false;
    public static final OutputHandler.ResultListener DEFAULT_RESULT_CALLBACK = null;

    Float minPoseDetectionConfidence;
    Float minPosePresenceConfidence;
    Float minTrackingConfidence;
    MODEL_BUNDLE currentModel;
    DELEGATE_HARDWARE currentDelegate;
    RunningMode runningMode;
    Context context;
    LandmarkerListener landmarkerListener;

    PoseDetectionHelper(RunningMode runningMode, Context context,
                        LandmarkerListener landmarkerListener) {
        this.minPoseDetectionConfidence = DEFAULT_MIN_POSE_DETECTION_CONFIDENCE;
        this.minPosePresenceConfidence = DEFAULT_MIN_POSE_PRESENCE_CONFIDENCE;
        this.minTrackingConfidence = DEFAULT_MIN_TRACKING_CONFIDENCE;
        this.currentModel = MODEL_BUNDLE.FULL;
        this.currentDelegate = DELEGATE_HARDWARE.CPU;
        this.runningMode = runningMode;
        this.context = context;
        this.landmarkerListener = landmarkerListener;
        setupPoseLandmarker();
    }
    PoseDetectionHelper(Float minPoseDetectionConfidence,
                        Float minPosePresenceConfidence,
                        Float minTrackingConfidence,
                        MODEL_BUNDLE currentModel,
                        DELEGATE_HARDWARE currentDelegate,
                        RunningMode runningMode,
                        Context context,
                        LandmarkerListener landmarkerListener
                        ) {

        this.minPoseDetectionConfidence = minPoseDetectionConfidence;
        this.minPosePresenceConfidence = minPosePresenceConfidence;
        this.minTrackingConfidence = minTrackingConfidence;
        this.currentModel = currentModel;
        this.currentDelegate = currentDelegate;
        this.runningMode = runningMode;
        this.context = context;
        this.landmarkerListener = landmarkerListener;
        setupPoseLandmarker();
    }

    private PoseLandmarker poseLandmarker = null;

    public void clearPoseLandmarker() {
        if (poseLandmarker!=null) poseLandmarker.close();
        poseLandmarker = null;
    }

    public boolean isClose() {
        return poseLandmarker == null;
    }

    public void setupPoseLandmarker() {
        BaseOptions.Builder baseOptionBuilder = BaseOptions.builder();

        switch (currentDelegate) {
            case CPU:
                baseOptionBuilder.setDelegate(Delegate.CPU);
                break;
            case GPU:
                baseOptionBuilder.setDelegate(Delegate.GPU);
                break;
            default:
                baseOptionBuilder.setDelegate(Delegate.CPU);
        }


    }

}
