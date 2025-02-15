package com.digiview.workwell.data.models;

import com.google.firebase.firestore.PropertyName;

import java.io.Serializable;
import java.util.List;

public class Exercise implements Serializable {

    private String ExerciseId; // Firestore ID
    private String Name;       // Exercise name
    private String Description; // Exercise description
    private String TargetArea; // Changed to string to align with Firestore
    private String OrganizationId; // ID of the organization the exercise belongs to
    private List<String> Constraints;
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

    public List<String> getConstraints() {
        return Constraints;
    }

    public void setConstraints(List<String> constraints) {
        Constraints = constraints;
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
