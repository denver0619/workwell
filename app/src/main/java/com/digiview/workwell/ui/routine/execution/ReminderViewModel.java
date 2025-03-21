package com.digiview.workwell.ui.routine.execution;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ReminderViewModel extends ViewModel {
    // TODO: Implement the ViewModel

    private final MutableLiveData<RoutineConstants.REMINDER_STATE> reminderState = new MutableLiveData<>();
    public LiveData<RoutineConstants.REMINDER_STATE> getReminderState() {
        return reminderState;
    }

    public void setReminderState(RoutineConstants.REMINDER_STATE reminderState) {
        this.reminderState.setValue(reminderState);
    }
}