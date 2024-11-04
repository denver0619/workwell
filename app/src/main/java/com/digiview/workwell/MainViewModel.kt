package com.digiview.workwell


import androidx.lifecycle.ViewModel
import com.digiview.workwell.services.mediapipe.PoseDetectionHelper

/**
 *  This ViewModel is used to store pose landmarker helper settings
 */
class MainViewModel : ViewModel() {

    private var _model = PoseDetectionHelper.MODEL_BUNDLE.FULL
    private var _delegate: PoseDetectionHelper.DELEGATE_HARDWARE = PoseDetectionHelper.DELEGATE_HARDWARE.CPU
    private var _minPoseDetectionConfidence: Float =
        PoseDetectionHelper.DEFAULT_MIN_POSE_DETECTION_CONFIDENCE
    private var _minPoseTrackingConfidence: Float = PoseDetectionHelper
        .DEFAULT_MIN_TRACKING_CONFIDENCE
    private var _minPosePresenceConfidence: Float = PoseDetectionHelper
        .DEFAULT_MIN_POSE_PRESENCE_CONFIDENCE

    val currentDelegate: PoseDetectionHelper.DELEGATE_HARDWARE get() = _delegate
    val currentModel: PoseDetectionHelper.MODEL_BUNDLE get() = _model
    val currentMinPoseDetectionConfidence: Float
        get() =
            _minPoseDetectionConfidence
    val currentMinPoseTrackingConfidence: Float
        get() =
            _minPoseTrackingConfidence
    val currentMinPosePresenceConfidence: Float
        get() =
            _minPosePresenceConfidence

    fun setDelegate(delegate: PoseDetectionHelper.DELEGATE_HARDWARE) {
        _delegate = delegate
    }

    fun setMinPoseDetectionConfidence(confidence: Float) {
        _minPoseDetectionConfidence = confidence
    }

    fun setMinPoseTrackingConfidence(confidence: Float) {
        _minPoseTrackingConfidence = confidence
    }

    fun setMinPosePresenceConfidence(confidence: Float) {
        _minPosePresenceConfidence = confidence
    }

    fun setModel(model: PoseDetectionHelper.MODEL_BUNDLE) {
        _model = model
    }
}