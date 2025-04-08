package com.venyou.dto;

public class OwnerDTO {
    private Long ownerId;
    private String name;

    public OwnerDTO(Long ownerId, String name) {
        this.ownerId = ownerId;
        this.name = name;
    }

    // Getters and Setters
    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
