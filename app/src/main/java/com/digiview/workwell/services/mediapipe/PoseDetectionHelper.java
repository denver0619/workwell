package com.digiview.workwell.services.mediapipe;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.SystemClock;
import android.util.Log;

import androidx.camera.core.ImageProxy;

import com.google.mediapipe.framework.image.BitmapImageBuilder;
import com.google.mediapipe.framework.image.MPImage;
import com.google.mediapipe.tasks.core.BaseOptions;
import com.google.mediapipe.tasks.core.Delegate;
import com.google.mediapipe.tasks.core.OutputHandler;
import com.google.mediapipe.tasks.vision.core.RunningMode;
import com.google.mediapipe.tasks.vision.poselandmarker.PoseLandmarker;
import com.google.mediapipe.tasks.vision.poselandmarker.PoseLandmarkerResult;

import java.util.Collections;
import java.util.List;

public class PoseDetectionHelper {
    // CONSTANT VALUES DECLARATION/INITIALIZATION
    public enum MODEL_BUNDLE {
        LITE("pose_landmarker_lite.task", 0),
        FULL("pose_landmarker_full.task", 1),
        HEAVY("pose_landmarker_heavy.task", 2);

        private final String value;
        private final int pos;

        MODEL_BUNDLE(String value, int pos) {
            this.value=value;
            this.pos = pos;
        }

        public String getValue() {
            return value;
        }

        public int getPos() {
            return pos;
        }
    }

    public enum DELEGATE_HARDWARE {
        CPU(0),
        GPU(1);

        private final int value;

        DELEGATE_HARDWARE(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }

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
        void onError(String error, int errorCode);
        void onResults(ResultBundle resultBundle);
    }

    //DATA CLASS (ENTITY)

    public class ResultBundle {
        private List<PoseLandmarkerResult> results;
        private Long inferenceTime;
        private int inputImageHeight;
        private int inputImageWidth;

        ResultBundle(List<PoseLandmarkerResult> results, Long inferenceTime, int inputImageHeight,int inputImageWidth) {
            this.results = results;
            this.inferenceTime = inferenceTime;
            this.inputImageHeight = inputImageHeight;
            this.inputImageWidth = inputImageWidth;
        }

        public List<PoseLandmarkerResult> getResults() {
            return results;
        }

        public Long getInferenceTime() {
            return inferenceTime;
        }

        public int getInputImageHeight() {
            return inputImageHeight;
        }

        public int getInputImageWidth() {
            return inputImageWidth;
        }
    }


    public static final String TAG = "PoseLandmarkerHelper";

    public static final int DEFAULT_NUM_POSES = 1;
    public static final float DEFAULT_MIN_POSE_DETECTION_CONFIDENCE = .5f;
    public static final float DEFAULT_MIN_POSE_PRESENCE_CONFIDENCE = .5f;
    public static final float DEFAULT_MIN_TRACKING_CONFIDENCE = .5f;
    public static final boolean DEFAULT_OUTPUT_SEGMENTATION_MASKS = false;
    public static final OutputHandler.ResultListener DEFAULT_RESULT_CALLBACK = null;

    public Float minPoseDetectionConfidence;
    public Float minPosePresenceConfidence;
    public Float minTrackingConfidence;
    public MODEL_BUNDLE currentModel = MODEL_BUNDLE.FULL;
    public DELEGATE_HARDWARE currentDelegate;
    public RunningMode runningMode;
    public  Context context;
    public LandmarkerListener landmarkerListener;

    public PoseDetectionHelper(RunningMode runningMode, Context context,
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
    public PoseDetectionHelper(
            Context context,
            RunningMode runningMode,
            Float minPoseDetectionConfidence,
            Float minTrackingConfidence,
            Float minPosePresenceConfidence,
            DELEGATE_HARDWARE currentDelegate,
            LandmarkerListener landmarkerListener
                        ) {

        this.minPoseDetectionConfidence = minPoseDetectionConfidence;
        this.minPosePresenceConfidence = minPosePresenceConfidence;
        this.minTrackingConfidence = minTrackingConfidence;
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

        String modelName;
        switch(currentModel) {
            case LITE:
                modelName = MODEL_BUNDLE.LITE.getValue();
                break;
            case FULL:
                modelName = MODEL_BUNDLE.FULL.getValue();
                break;
            case HEAVY:
                modelName = MODEL_BUNDLE.HEAVY.getValue();
                break;
            default:
                modelName = MODEL_BUNDLE.FULL.getValue();
        }

        baseOptionBuilder.setModelAssetPath(modelName);

        switch (runningMode) {
            case LIVE_STREAM:
                if (landmarkerListener == null) {
                    throw new IllegalStateException("landmarkerListener must be set when runningMode is LIVE_STREAM.");
                }
                break;
            default:
        }
        try {
            BaseOptions baseOptions = baseOptionBuilder.build();
            PoseLandmarker.PoseLandmarkerOptions.Builder optionsBuilder = PoseLandmarker
                    .PoseLandmarkerOptions
                    .builder()
                    .setBaseOptions(baseOptions)
                    .setMinPoseDetectionConfidence(minPoseDetectionConfidence)
                    .setMinTrackingConfidence(minTrackingConfidence)
                    .setMinPosePresenceConfidence(minPosePresenceConfidence)
                    .setRunningMode(runningMode);

            if (runningMode == RunningMode.LIVE_STREAM) {
                optionsBuilder.setResultListener(this::returnLivestreamResult)
                        .setErrorListener(this::returnLivestreamError);
            }

            PoseLandmarker.PoseLandmarkerOptions options = optionsBuilder.build();
            PoseLandmarker.createFromOptions(context, options);
        } catch (IllegalStateException e) {
            if (landmarkerListener != null) {
                landmarkerListener.onError(
                        "Pose Landmarker failed to initialize. See error logs for " +
                                "details");
            }
            Log.e(
                    TAG, "MediaPipe failed to load the task with error: " + e
                            .getMessage()
            );
        }
    }

    private void returnLivestreamResult (PoseLandmarkerResult result, MPImage input) {
        long finishTimeMs = SystemClock.uptimeMillis();
        long inferenceTime = finishTimeMs - result.timestampMs();

        if (landmarkerListener != null) {
            landmarkerListener.onResults(
                    new ResultBundle(
                            Collections.singletonList(result), // Converts result to a list
                            inferenceTime,
                            input.getHeight(),
                            input.getWidth()
                    )
            );
        }
    }

    private void returnLivestreamError(RuntimeException error) {
        if (landmarkerListener != null) {
            String errorMessage = (error.getMessage() != null) ? error.getMessage() : "An unknown error has occurred";
            landmarkerListener.onError(errorMessage);
        }
    }

    public void detectLiveStream(ImageProxy imageProxy, Boolean isFrontCamera) {
        if (runningMode != RunningMode.LIVE_STREAM) {
            throw new IllegalArgumentException(
                    "Attempting to call detectLiveStream" +
                            " while not using RunningMode.LIVE_STREAM");
        }
        Long frameTime = SystemClock.uptimeMillis();

        Bitmap bitmapBuffer = Bitmap.createBitmap(
                imageProxy.getWidth(),
                imageProxy.getHeight(),
                Bitmap.Config.ARGB_8888
        );

        try (ImageProxy image = imageProxy) {
            bitmapBuffer.copyPixelsFromBuffer(image.getPlanes()[0].getBuffer());
        }

        Matrix matrix = new Matrix();
        matrix.postRotate((float) imageProxy.getImageInfo().getRotationDegrees());

        // Flip image if user uses front camera
        if (isFrontCamera) {
            matrix.postScale(
                    -1f,
                    1f,
                    (float) imageProxy.getWidth(),
                    (float) imageProxy.getHeight()
            );
        }

        Bitmap rotatedBitmap = Bitmap.createBitmap(bitmapBuffer, 0,0, bitmapBuffer.getWidth(), bitmapBuffer.getHeight(), matrix, true);

        MPImage mpImage = new BitmapImageBuilder(rotatedBitmap).build();

        detectAsync(mpImage,frameTime);

    }

    public void detectAsync(MPImage mpImage, Long frameTime) {
        if (poseLandmarker != null) {
            poseLandmarker.detectAsync(mpImage,frameTime);
        }
    }

}
