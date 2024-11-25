package com.digiview.workwell.data.models;

import com.google.firebase.firestore.PropertyName;

public class SelfAssessment {

    @PropertyName("SelfAssessmentId")
    private String selfAssessmentId;

    @PropertyName("RoutineLogId")
    private String routineLogId;

    @PropertyName("Awareness")
    private int awareness;

    @PropertyName("Difficulty")
    private int difficulty;

    @PropertyName("Pain")
    private int pain;

    @PropertyName("Stiffness")
    private int stiffness;

    // Getters and setters with @PropertyName for serialization/deserialization
    @PropertyName("SelfAssessmentId")
    public String getSelfAssessmentId() {
        return selfAssessmentId;
    }

    @PropertyName("SelfAssessmentId")
    public void setSelfAssessmentId(String selfAssessmentId) {
        this.selfAssessmentId = selfAssessmentId;
    }

    @PropertyName("RoutineLogId")
    public String getRoutineLogId() {
        return routineLogId;
    }

    @PropertyName("RoutineLogId")
    public void setRoutineLogId(String routineLogId) {
        this.routineLogId = routineLogId;
    }

    @PropertyName("Awareness")
    public int getAwareness() {
        return awareness;
    }

    @PropertyName("Awareness")
    public void setAwareness(int awareness) {
        this.awareness = awareness;
    }

    @PropertyName("Difficulty")
    public int getDifficulty() {
        return difficulty;
    }

    @PropertyName("Difficulty")
    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    @PropertyName("Pain")
    public int getPain() {
        return pain;
    }

    @PropertyName("Pain")
    public void setPain(int pain) {
        this.pain = pain;
    }

    @PropertyName("Stiffness")
    public int getStiffness() {
        return stiffness;
    }

    @PropertyName("Stiffness")
    public void setStiffness(int stiffness) {
        this.stiffness = stiffness;
    }
}
