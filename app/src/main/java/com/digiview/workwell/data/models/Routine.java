package com.digiview.workwell.data.models;

import com.google.firebase.firestore.PropertyName;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Routine implements Serializable {
    private String RoutineId;
    private String Name;
    private String TargetArea;
    private int Frequency;
    private List<String> Users;
    private List<RoutineExercise> Exercises;
    private String AssignedName;
    @PropertyName("StartDate")
    private Date StartDate;
    @PropertyName("EndDate")
    private Date EndDate;

    public String getFormattedStartDate() {
        if (StartDate != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
            return dateFormat.format(StartDate);
        }
        return null;
    }

    public void setFormattedStartDate(String formattedStartDate) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
            this.StartDate = dateFormat.parse(formattedStartDate);
        } catch (Exception e) {
            e.printStackTrace(); // Handle parsing errors
            this.StartDate = null;
        }
    }

    public String getFormattedEndDate() {
        if (EndDate != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
            return dateFormat.format(EndDate);
        }
        return null;
    }

    public void setFormattedEndDate(String formattedEndDate) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
            this.EndDate = dateFormat.parse(formattedEndDate);
        } catch (Exception e) {
            e.printStackTrace(); // Handle parsing errors
            this.EndDate = null;
        }
    }

    // Getters and setters
    public String getRoutineId() {
        return RoutineId;
    }

    public void setRoutineId(String routineId) {
        this.RoutineId = routineId;
    }

    public String getName() {
        return Name;
    }



    public void setName(String name) {
        this.Name = name;
    }

    public int getFrequency() {
        return Frequency;
    }
    public void setFrequency(int Frequency) {
        this.Frequency = Frequency;
    }

    public Date getStartDate() {
        return StartDate;
    }

    public void setStartDate(Date startDate) {
        this.StartDate = startDate;
    }

    public Date getEndDate() {
        return EndDate;
    }

    public void setEndDate(Date endDate) {
        this.EndDate = endDate;
    }

    @PropertyName("TargetArea")
    public String getTargetArea() {
        return TargetArea;
    }

    @PropertyName("TargetArea")
    public void setTargetArea(String targetArea) {
        this.TargetArea = targetArea;
    }

    public List<String> getUsers() {
        return Users;
    }

    public void setUsers(List<String> users) {
        this.Users = users;
    }

    public List<RoutineExercise> getExercises() {
        return Exercises;
    }

    public void setExercises(List<RoutineExercise> exercises) {
        this.Exercises = exercises;
    }

    public String getAssignedName() {
        return AssignedName;
    }

    public void setAssignedName(String assignedName) {
        this.AssignedName = assignedName;
    }

    @Override
    public String toString() {
        return "Routine{" +
                "RoutineId='" + RoutineId + '\'' +
                ", Name='" + Name + '\'' +
                ", Users=" + Users +
                ", TargetArea='" + TargetArea + '\'' +
                ", Exercises=" + (Exercises != null ? Exercises.toString() : "No exercises available") +
                '}';
    }
}
