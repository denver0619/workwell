package com.digiview.workwell.data.models;

import com.google.firebase.firestore.PropertyName;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class RoutineLogs {

    @PropertyName("RoutineLogId")
    private String routineLogId;

    @PropertyName("RoutineId")
    private String routineId;

    @PropertyName("RoutineLogName")
    private String routineLogName;

    @PropertyName("Uid")
    private String uid;

    @PropertyName("CreatedAt")
    private Date createdAt;

    // Default constructor for Firestore
    public RoutineLogs() {
    }

    // Constructor with all fields
    public RoutineLogs(String routineLogId, String routineId, String routineLogName, String uid, Date createdAt) {
        this.routineLogId = routineLogId;
        this.routineId = routineId;
        this.routineLogName = routineLogName;
        this.uid = uid;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    @PropertyName("RoutineLogId")
    public String getRoutineLogId() {
        return routineLogId;
    }

    @PropertyName("RoutineLogId")
    public void setRoutineLogId(String routineLogId) {
        this.routineLogId = routineLogId;
    }

    @PropertyName("RoutineId")
    public String getRoutineId() {
        return routineId;
    }

    @PropertyName("RoutineId")
    public void setRoutineId(String routineId) {
        this.routineId = routineId;
    }

    @PropertyName("RoutineLogName")
    public String getRoutineLogName() {
        return routineLogName;
    }

    @PropertyName("RoutineLogName")
    public void setRoutineLogName(String routineLogName) {
        this.routineLogName = routineLogName;
    }

    @PropertyName("Uid")
    public String getUid() {
        return uid;
    }

    @PropertyName("Uid")
    public void setUid(String uid) {
        this.uid = uid;
    }

    @PropertyName("CreatedAt")
    @ServerTimestamp
    public Date getCreatedAt() {
        return createdAt;
    }

    @PropertyName("CreatedAt")
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "RoutineLogs{" +
                "RoutineLogId='" + routineLogId + '\'' +
                ", RoutineId='" + routineId + '\'' +
                ", RoutineLogName='" + routineLogName + '\'' +
                ", Uid='" + uid + '\'' +
                ", CreatedAt=" + createdAt +
                '}';
    }
}
