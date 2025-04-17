package com.venyou.model;

import java.time.LocalDateTime;

public class Brand {
    private long brandId;
    private String name;
    private String description;
    private LocalDateTime createdAt;

    // Default constructor
    public Brand() {
        this.createdAt = LocalDateTime.now();
    }

    // Constructor with name only (for dropdowns/mock data)
    public Brand(String name) {
        this.name = name;
        this.createdAt = LocalDateTime.now();
    }

    // Full constructor
    public Brand(long brandId, String name, String description, LocalDateTime createdAt) {
        this.brandId = brandId;
        this.name = name;
        this.description = description;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public long getBrandId() { return brandId; }
    public void setBrandId(long brandId) { this.brandId = brandId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public void setBrandName(String brandName) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setBrandName'");
    }
}
