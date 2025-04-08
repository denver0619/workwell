package com.digiview.workwell.ui.routine.execution;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.digiview.workwell.services.tts.TTSHelper;
import com.digiview.workwell.services.tts.TTSInitializationListener;

import java.io.File;

public class VideoConvertViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    private final MutableLiveData<Boolean> isConvertSuccess = new MutableLiveData<>(false);
    public LiveData<Boolean> getIsConvertSuccess() {
        return isConvertSuccess;
    }
    public void setIsConvertSuccess(Boolean isConvertSuccess) {
        this.isConvertSuccess.postValue(isConvertSuccess);
    }

    // In VideoConvertViewModel.java
    private File outputVideoFile;

    public File getOutputVideoFile() {
        return outputVideoFile;
    }

    public void setOutputVideoFile(File outputVideoFile) {
        this.outputVideoFile = outputVideoFile;
    }

    private final MutableLiveData<TTSHelper> ttsHelper = new MutableLiveData<>();
    public void setTtsHelper(Context context, TTSInitializationListener listener) {
        ttsHelper.setValue(new TTSHelper(context, listener));
    }

    public LiveData<TTSHelper> getTtsHelper() {
        return ttsHelper;
    }

}