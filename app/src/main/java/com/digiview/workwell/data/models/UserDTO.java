package com.digiview.workwell.data.models;

public class UserDTO {
    private String uid;
    private String email;
    private String name;
    private int age;
    private String contact;
    private double height;
    private double weight;
    private String address;
    private String assignedProfessional; // ID
    private String assignedProfessionalName; // Full name from Users collection

    public UserDTO() {}

    public UserDTO(String uid, String email, String name, int age, String contact, double height, double weight, String address, String assignedProfessional, String assignedProfessionalName) {
        this.uid = uid;
        this.email = email;
        this.name = name;
        this.age = age;
        this.contact = contact;
        this.height = height;
        this.weight = weight;
        this.address = address;
        this.assignedProfessional = assignedProfessional;
        this.assignedProfessionalName = assignedProfessionalName;
    }

    // Getters and Setters
    public String getUid() { return uid; }
    public void setUid(String uid) { this.uid = uid; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

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

    public String getAssignedProfessionalName() { return assignedProfessionalName; }
    public void setAssignedProfessionalName(String assignedProfessionalName) { this.assignedProfessionalName = assignedProfessionalName; }
}
