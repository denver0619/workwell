package com.digiview.workwell.data.models;

import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.PropertyName;
import com.google.firebase.firestore.ServerTimestamp;

import java.io.Serializable;
import java.util.Date;

public class RoutineLogs implements Serializable {

    @PropertyName("RoutineLogId")
    private String routineLogId;

    @PropertyName("RoutineId")
    private String routineId;

    @PropertyName("RoutineLogName")
    private String routineLogName;

    @PropertyName("Uid")
    private String uid;

    @PropertyName("SelfAssessmentId")
    private String selfAssessmentId;

    @PropertyName("VideoId")
    private String videoId;

    @PropertyName("JournalId")
    private String journalId;

    @PropertyName("OrganizationId")
    private String organizationId; // New field for organization ID

    @PropertyName("Comment")
    private String comment;

    @ServerTimestamp
    @PropertyName("CreatedAt")
    private Date createdAt;

    // Default constructor for Firestore
    public RoutineLogs() {
    }

    // Associated models for easier handling
    @Exclude
    private SelfAssessment selfAssessment;
    @Exclude
    private Video video;
    @Exclude
    private Journal journal;

    // Constructor with all fields
    public RoutineLogs(String routineLogId, String routineId, String routineLogName, String uid, String organizationId, Date createdAt, String comment) {
        this.routineLogId = routineLogId;
        this.routineId = routineId;
        this.routineLogName = routineLogName;
        this.uid = uid;
        this.organizationId = organizationId;
        this.comment = comment;
        this.createdAt = createdAt;
    }

    // Constructor with all fields
    public RoutineLogs(String routineLogId, String routineId, String routineLogName, String uid, String organizationId, Date createdAt) {
        this.routineLogId = routineLogId;
        this.routineId = routineId;
        this.routineLogName = routineLogName;
        this.uid = uid;
        this.organizationId = organizationId;
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

    @PropertyName("SelfAssessmentId")
    public String getSelfAssessmentId() {
        return selfAssessmentId;
    }

    @PropertyName("SelfAssessmentId")
    public void setSelfAssessmentId(String selfAssessmentId) {
        this.selfAssessmentId = selfAssessmentId;
    }

    @PropertyName("VideoId")
    public String getVideoId() {
        return videoId;
    }

    @PropertyName("VideoId")
    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    @PropertyName("JournalId")
    public String getJournalId() {
        return journalId;
    }

    @PropertyName("JournalId")
    public void setJournalId(String journalId) {
        this.journalId = journalId;
    }

    @PropertyName("OrganizationId")
    public String getOrganizationId() {
        return organizationId;
    }

    @PropertyName("OrganizationId")
    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    @PropertyName("Comment")
    public String getComment() {
        return comment;
    }

    @PropertyName("Comment")
    public void setComment(String comment) {
        this.comment = comment;
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
                ", OrganizationId='" + organizationId + '\'' +
                ", CreatedAt=" + createdAt +
                '}';
    }

    public SelfAssessment getSelfAssessment() {
        return selfAssessment;
    }

    public void setSelfAssessment(SelfAssessment selfAssessment) {
        this.selfAssessment = selfAssessment;
    }

    public Video getVideo() {
        return video;
    }

    public void setVideo(Video video) {
        this.video = video;
    }

    public Journal getJournal() {
        return journal;
    }

    public void setJournal(Journal journal) {
        this.journal = journal;
    }
}
