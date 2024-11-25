package com.digiview.workwell.data.models;

import com.google.firebase.firestore.PropertyName;
import com.digiview.workwell.data.models.TargetArea;

public class Exercise {

    private String ExerciseId; // Firestore ID
    private String Name;       // Exercise name
    private String Description; // Exercise description
    private TargetArea TargetArea; // Converted from enum (numeric) to string

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
    public TargetArea getTargetArea() {
        return TargetArea;
    }

    @PropertyName("TargetArea")
    public void setTargetArea(Long targetAreaValue) {
        this.TargetArea = com.digiview.workwell.data.models.TargetArea.fromValue(targetAreaValue);
    }
}
