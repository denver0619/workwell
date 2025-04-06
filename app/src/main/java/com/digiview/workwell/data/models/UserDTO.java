package com.digiview.workwell.data.models;

import java.text.SimpleDateFormat;
import java.util.Date;

public class UserDTO {
    private String uid;
    private String email;
    private String name;
    private Date birthDate;
    private String contact;
    private String sex;
    private double height;
    private double weight;
    private String address;
    private String assignedProfessional; // ID
    private String assignedProfessionalName; // Full name from Users collection

    public UserDTO() {}

    public UserDTO(String uid, String email, String name, Date birthDate, String contact, double height, double weight, String sex, String address, String assignedProfessional, String assignedProfessionalName) {
        this.uid = uid;
        this.email = email;
        this.name = name;
        this.birthDate = birthDate;
        this.contact = contact;
        this.height = height;
        this.weight = weight;
        this.sex = sex;
        this.address = address;
        this.assignedProfessional = assignedProfessional;
        this.assignedProfessionalName = assignedProfessionalName;
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

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Date getBirthDate() { return birthDate; }
    public void setBirthDate(Date birthDate) { this.birthDate = birthDate; }

    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }

    public double getHeight() { return height; }
    public void setHeight(double height) { this.height = height; }

    public double getWeight() { return weight; }
    public void setWeight(double weight) { this.weight = weight; }

    public String getSex() { return sex; }
    public void setSex(String sex) { this.sex = sex; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getAssignedProfessional() { return assignedProfessional; }
    public void setAssignedProfessional(String assignedProfessional) { this.assignedProfessional = assignedProfessional; }

    public String getAssignedProfessionalName() { return assignedProfessionalName; }
    public void setAssignedProfessionalName(String assignedProfessionalName) { this.assignedProfessionalName = assignedProfessionalName; }
}
