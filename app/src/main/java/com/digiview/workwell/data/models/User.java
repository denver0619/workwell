package com.digiview.workwell.data.models;

import com.google.firebase.firestore.PropertyName;

public class User {
    @PropertyName("Uid")
    private String uid;

    @PropertyName("Email")
    private String email;

    @PropertyName("FirstName")
    private String firstName;

    @PropertyName("LastName")
    private String lastName;

    @PropertyName("Age")
    private int age;

    @PropertyName("Contact")
    private String contact;

    @PropertyName("Height")
    private double height;

    @PropertyName("Weight")
    private double weight;

    @PropertyName("Address")
    private String address;

    @PropertyName("AssignedProfessional")
    private String assignedProfessional;

    // Empty constructor required by Firestore
    public User() {}

    public User(String uid, String email, String firstName, String lastName, int age, String contact, double height, double weight, String address, String assignedProfessional) {
        this.uid = uid;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.contact = contact;
        this.height = height;
        this.weight = weight;
        this.address = address;
        this.assignedProfessional = assignedProfessional;
    }

    // Getters and Setters
    public String getUid() { return uid; }
    public void setUid(String uid) { this.uid = uid; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }

    public double getHeight() { return height; }
    public void setHeight(double height) { this.height = height; }

    public double getWeight() { return weight; }
    public void setWeight(double weight) { this.weight = weight; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getAssignedProfessional() { return assignedProfessional; }
    public void setAssignedProfessional(String assignedProfessional) { this.assignedProfessional = assignedProfessional; }
}
