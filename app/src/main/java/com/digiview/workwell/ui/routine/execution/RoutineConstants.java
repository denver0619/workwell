package com.digiview.workwell.ui.routine.execution;

public class RoutineConstants {
    public enum EXECUTION_STATE {
        PREPARING,
        ONGOING,
        FINISHED,
        STOPPED,
        PAUSED,
        NONE;
    }
    public enum TRANSITION_STATE{
        ONGOING,
        FINISHED,
        PENDING,
        NONE;
    }
    public enum REMINDER_STATE{
        IGNORED,
        ONGOING,
        ACCEPTED,
        NONE;
    }
    public enum CONFIRMATION_STATE{
        IGNORED,
        ONGOING,
        ACCEPTED,
        REJECTED,
        NONE;
    }
}
