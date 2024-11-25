package com.digiview.workwell.data.models;

import com.google.firebase.firestore.PropertyName;

import java.io.Serializable;
import java.util.List;

public class Routine implements Serializable {
    private String RoutineId;
    private String Name;
    private TargetArea TargetArea;
    private List<String> Users;
    private List<RoutineExercise> Exercises;
    private String AssignedName;

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

    @PropertyName("TargetArea")
    public TargetArea getTargetArea() {
        return TargetArea;
    }

    @PropertyName("TargetArea")
    public void setTargetArea(Long targetAreaValue) {
        this.TargetArea = com.digiview.workwell.data.models.TargetArea.fromValue(targetAreaValue);
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

//    // Fetch assigned name asynchronously
//    public CompletableFuture<String> fetchAssignedName() {
//        CompletableFuture<String> future = new CompletableFuture<>();
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        db.collection("users")
//                .document(Users)
//                .get()
//                .addOnSuccessListener(document -> {
//                    if (document.exists()) {
//                        String firstName = document.getString("FirstName");
//                        String lastName = document.getString("LastName");
//                        future.complete(firstName + " " + lastName);
//                    } else {
//                        future.complete("Unknown User");
//                    }
//                })
//                .addOnFailureListener(e -> future.completeExceptionally(e));
//
//        return future;
//    }

    @Override
    public String toString() {
        return "Routine{" +
                "RoutineId='" + RoutineId + '\'' +
                ", Name='" + Name + '\'' +
                ", Users='" + Users + '\'' +
                ", TargetArea=" + TargetArea +
                ", Exercises=" + (Exercises != null ? Exercises.toString() : "No exercises available") +
                '}';
    }
}
