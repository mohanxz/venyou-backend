package com.venyou.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BrandRequest {
    private String name;
    private String description;
    private Long ownerId; 
}
