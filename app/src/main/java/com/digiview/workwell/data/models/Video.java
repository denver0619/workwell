package com.digiview.workwell.data.models;

import com.google.firebase.firestore.PropertyName;

public class Video {
    @PropertyName("VideoId")
    private String videoId;

    @PropertyName("CloudinaryId")
    private String cloudinaryId;

    @PropertyName("VideoUrl")
    private String videoUrl;

    @PropertyName("OrganizationId")
    private String organizationId; // New field for OrganizationId

    @PropertyName("VideoId")
    public String getVideoId() {
        return videoId;
    }

    @PropertyName("VideoId")
    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    @PropertyName("CloudinaryId")
    public String getCloudinaryId() {
        return cloudinaryId;
    }

    @PropertyName("CloudinaryId")
    public void setCloudinaryId(String cloudinaryId) {
        this.cloudinaryId = cloudinaryId;
    }

    @PropertyName("VideoUrl")
    public String getVideoUrl() {
        return videoUrl;
    }

    @PropertyName("VideoUrl")
    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    @PropertyName("OrganizationId")
    public String getOrganizationId() {
        return organizationId;
    }

    @PropertyName("OrganizationId")
    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    @Override
    public String toString() {
        return "Video{" +
                "videoId='" + videoId + '\'' +
                ", cloudinaryId='" + cloudinaryId + '\'' +
                ", videoUrl='" + videoUrl + '\'' +
                ", organizationId='" + organizationId + '\'' +
                '}';
    }
}
