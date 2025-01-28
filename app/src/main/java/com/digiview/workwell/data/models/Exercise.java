package com.digiview.workwell.data.models;

import com.google.firebase.firestore.PropertyName;

public class Exercise {

    private String ExerciseId; // Firestore ID
    private String Name;       // Exercise name
    private String Description; // Exercise description
    private String TargetArea; // Changed to string to align with Firestore
    private String OrganizationId; // ID of the organization the exercise belongs to

    // Default constructor (required for Firestore)
    public Exercise() {
    }

    // Getters and Setters
    public String getExerciseId() {
        return ExerciseId;
    }

    public void setExerciseId(String exerciseId) {
        ExerciseId = exerciseId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    @PropertyName("TargetArea")
    public String getTargetArea() {
        return TargetArea;
    }

    @PropertyName("TargetArea")
    public void setTargetArea(String targetArea) {
        this.TargetArea = targetArea; // Set string value directly
    }

    @PropertyName("OrganizationId")
    public String getOrganizationId() {
        return OrganizationId;
    }

    @PropertyName("OrganizationId")
    public void setOrganizationId(String organizationId) {
        this.OrganizationId = organizationId; // Set the organization ID
    }

    @Override
    public String toString() {
        return "Exercise{" +
                "ExerciseId='" + ExerciseId + '\'' +
                ", Name='" + Name + '\'' +
                ", Description='" + Description + '\'' +
                ", TargetArea='" + TargetArea + '\'' +
                ", OrganizationId='" + OrganizationId + '\'' +
                '}';
    }
}
