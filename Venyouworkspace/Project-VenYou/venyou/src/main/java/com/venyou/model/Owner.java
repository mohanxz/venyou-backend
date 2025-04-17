package com.venyou.model;

public class Owner {
    private int ownerId;
    private String name;
    private String email;
    private String phone;
    private String aadharNumber;
    private String createdAt;

    // Default constructor
    public Owner() {}

    // Constructor with name only (for dropdowns/mock data)
    public Owner(String name) {
        this.name = name;
    }

    // Getters and Setters
    public int getOwnerId() { return ownerId; }
    public void setOwnerId(int ownerId) { this.ownerId = ownerId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAadharNumber() { return aadharNumber; }
    public void setAadharNumber(String aadharNumber) { this.aadharNumber = aadharNumber; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
}
