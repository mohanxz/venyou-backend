package com.venyou.service.dto;

import lombok.*;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
    
    // Owner fields
    private Long ownerId;
    private String ownerName;
    private String ownerEmail;
    private String ownerPhone;
    private String ownerAadhar;
    
    // Category fields
    private Long categoryId;
    private String categoryName;
    
    // Brand fields
    private Long brandId;
    private String brandName;
    
    // Media fields
    private String mapEmbedUrl;
    private String featureBannerImage;
    private String videoSrc;
    private String virtualTourMap;
    
    private List<String> imagePaths;
    private List<String> amenities;

    // Builder with defaults
    public static HallRequestBuilder builder() {
        return new HallRequestBuilder()
                .totalRooms(0)
                .roomPrice(BigDecimal.ZERO)
                .imagePaths(List.of())
                .amenities(List.of());
    }
}