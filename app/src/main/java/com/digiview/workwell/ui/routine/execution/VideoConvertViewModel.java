package com.digiview.workwell.ui.routine.execution;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class VideoConvertViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    private final MutableLiveData<Boolean> isConvertSuccess = new MutableLiveData<>(false);
    public LiveData<Boolean> getIsConvertSuccess() {
        return isConvertSuccess;
    }
    public void setIsConvertSuccess(Boolean isConvertSuccess) {
        this.isConvertSuccess.postValue(isConvertSuccess);
    }
}