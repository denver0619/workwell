package com.digiview.workwell.ui.routine.execution;

import android.content.Context;

import androidx.camera.core.CameraProvider;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.Preview;
import androidx.camera.core.VideoCapture;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.digiview.workwell.services.mediapipe.PoseLandmarkerHelper;
import com.google.mediapipe.tasks.vision.poselandmarker.PoseLandmarkerResult;

import java.io.File;

public class CameraViewModel extends ViewModel {

    // MY MODIFICATION=====================================
    // for calculation
    private final MutableLiveData<PoseLandmarkerResult> landmarkerResult = new MutableLiveData<>();
    public LiveData<PoseLandmarkerResult> getLandmarkerResult() {
        return landmarkerResult;
    }

    public void setLandmarkerResult(PoseLandmarkerResult landmarkerResult) {
        this.landmarkerResult.setValue(landmarkerResult);
    }
    // FOR CAMERAFRAGMENT
    private int _model = PoseLandmarkerHelper.MODEL_POSE_LANDMARKER_FULL;
    private int _delegate = PoseLandmarkerHelper.DELEGATE_GPU;
    private float _minPoseDetectionConfidence = PoseLandmarkerHelper.DEFAULT_POSE_DETECTION_CONFIDENCE;
    private float _minPoseTrackingConfidence = PoseLandmarkerHelper.DEFAULT_POSE_TRACKING_CONFIDENCE;
    private float _minPosePresenceConfidence = PoseLandmarkerHelper.DEFAULT_POSE_PRESENCE_CONFIDENCE;

    // Getters for current values (equivalent to Kotlin's "val" properties)
    public int getCurrentDelegate() {
        return _delegate;
    }

    public int getCurrentModel() {
        return _model;
    }

    public float getCurrentMinPoseDetectionConfidence() {
        return _minPoseDetectionConfidence;
    }

    public float getCurrentMinPoseTrackingConfidence() {
        return _minPoseTrackingConfidence;
    }

    public float getCurrentMinPosePresenceConfidence() {
        return _minPosePresenceConfidence;
    }

    // Setters for updating values
    public void setDelegate(int delegate) {
        this._delegate = delegate;
    }

    public void setMinPoseDetectionConfidence(float confidence) {
        this._minPoseDetectionConfidence = confidence;
    }

    public void setMinPoseTrackingConfidence(float confidence) {
        this._minPoseTrackingConfidence = confidence;
    }

    public void setMinPosePresenceConfidence(float confidence) {
        this._minPosePresenceConfidence = confidence;
    }

    public void setModel(int model) {
        this._model = model;
    }

    // CAMERA STUFF

    // IO OPERATIONS
    //TODO: INITIALIZE THIS VALUES IN ROUTINEFRAGMENT
    private File _saveDirectory;
    private String _fitnessLogID;
    public void setSaveDirectory(Context context, String fitnessLogID) {
        // Get the app's private directory (internal storage)
        File appDirectory = context.getCacheDir();

        // Create a subfolder (e.g., "recordings") inside the app's private directory
        File subfolder = new File(appDirectory, fitnessLogID);

        // If the subfolder doesn't exist, create it
        if (!subfolder.exists()) {
            subfolder.mkdir();// Create the directory
        }
        _saveDirectory = subfolder;
    }

    public File getSaveDirectory(){
        return _saveDirectory;
    }


    public void setFitnessLogID(String fileName) {
        _fitnessLogID = fileName;
    }

    public String getFitnessLogID() {
        return _fitnessLogID;
    }

}

