package com.digiview.workwell.data.models;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class RoutineLogs {

    private String RoutineLogId;
    private String RoutineId;
    private String RoutineLogName;
    private String Uid;
    private Date CreatedAt;

    // Default constructor for Firestore
    public RoutineLogs() {
    }

    // Constructor with all fields
    public RoutineLogs(String routineLogId, String routineId, String routineLogName, String uid, Date createdAt) {
        this.RoutineLogId = routineLogId;
        this.RoutineId = routineId;
        this.RoutineLogName = routineLogName;
        this.Uid = uid;
        this.CreatedAt = createdAt;
    }

    // Getters and Setters
    public String getRoutineLogId() {
        return RoutineLogId;
    }

    public void setRoutineLogId(String routineLogId) {
        this.RoutineLogId = routineLogId;
    }

    public String getRoutineId() {
        return RoutineId;
    }

    public void setRoutineId(String routineId) {
        this.RoutineId = routineId;
    }

    public String getRoutineLogName() {
        return RoutineLogName;
    }

    public void setRoutineLogName(String routineLogName) {
        this.RoutineLogName = routineLogName;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        this.Uid = uid;
    }

    @ServerTimestamp
    public Date getCreatedAt() {
        return CreatedAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.CreatedAt = createdAt;
    }

    @Override
    public String toString() {
        return "RoutineLogs{" +
                "RoutineLogId='" + RoutineLogId + '\'' +
                ", RoutineId='" + RoutineId + '\'' +
                ", RoutineLogName='" + RoutineLogName + '\'' +
                ", Uid='" + Uid + '\'' +
                ", CreatedAt=" + CreatedAt +
                '}';
    }
}
