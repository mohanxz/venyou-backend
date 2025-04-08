package com.venyou.dto;

public class BrandDTO {
    private Long brandId;
    private String name;

    public BrandDTO(Long brandId, String name) {
        this.brandId = brandId;
        this.name = name;
    }

    // Getters and Setters
    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
