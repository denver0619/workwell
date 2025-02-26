package com.digiview.workwell.data.models;

import com.google.firebase.firestore.PropertyName;

import java.text.SimpleDateFormat;
import java.util.Date;

public class User {
    @PropertyName("Uid")
    private String uid;

    @PropertyName("Email")
    private String email;

    @PropertyName("FirstName")
    private String firstName;

    @PropertyName("LastName")
    private String lastName;

    @PropertyName("BirthDate")
    private Date birthDate;

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

    public User(String uid, String email, String firstName, String lastName, Date birthDate, String contact, double height, double weight, String address, String assignedProfessional) {
        this.uid = uid;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.contact = contact;
        this.height = height;
        this.weight = weight;
        this.address = address;
        this.assignedProfessional = assignedProfessional;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    // Date formatting methods
    public String getFormattedBirthDate() {
        return formatDate(birthDate);
    }

    public void setFormattedBirthDate(String formattedDate) {
        this.birthDate = parseDate(formattedDate);
    }

    private String formatDate(Date date) {
        if (date != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
            return dateFormat.format(date);
        }
        return null;
    }

    private Date parseDate(String formattedDate) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
            return dateFormat.parse(formattedDate);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
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
