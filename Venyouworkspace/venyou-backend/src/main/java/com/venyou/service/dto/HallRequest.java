package com.venyou.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HallRequest {

    private String name;
    private Integer capacity;
    private Integer totalRooms;
    private BigDecimal roomPrice;
    private String roomInfo;
    private BigDecimal price;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String postalCode;
    private String country;
    private String description;
    
    private Long ownerId;

    private Long categoryId;
    private String categoryName;

    private Long brandId;
    private String brandName;

    private String mapEmbedUrl;

    private List<String> imagePaths; // 🎯 Now added
}
