package com.digiview.workwell.ui.routine.execution;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ConfirmationStartRoutineViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    private final MutableLiveData<RoutineConstants.CONFIRMATION_STATE> confirmationState = new MutableLiveData<>();
    public LiveData<RoutineConstants.CONFIRMATION_STATE> getConfirmationState() {
        return confirmationState;
    }
    public void setConfirmationState(RoutineConstants.CONFIRMATION_STATE confirmationState) {
        this.confirmationState.setValue(confirmationState);
    }
}