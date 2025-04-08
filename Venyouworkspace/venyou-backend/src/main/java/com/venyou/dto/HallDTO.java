package com.venyou.dto;

import com.venyou.model.Hall;
import com.venyou.model.Hall.Status;

import java.math.BigDecimal;
import java.util.List;

public class HallDTO {

    private Long hallId;
    private String name;
    private String ownerName;
    private String brandName;
    private String categoryName;

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
    private Status status;

    private List<String> imagePaths;
    private String virtualTourMap;
    private String mapEmbedUrl;
    private String featureBannerImage;
    private String videoSrc;

    private List<String> amenities;
    private double averageRating;

    // --- Constructor from Hall Entity ---
    public HallDTO(Hall hall) {
        this.hallId = hall.getHallId();
        this.name = hall.getName();
        this.ownerName = hall.getOwner() != null ? hall.getOwner().getName() : null;
        this.brandName = hall.getBrand() != null ? hall.getBrand().getName() : null;
        this.categoryName = hall.getCategory() != null ? hall.getCategory().getCategoryName() : null;

        this.capacity = hall.getCapacity();
        this.totalRooms = hall.getTotalRooms();
        this.roomPrice = hall.getRoomPrice();
        this.roomInfo = hall.getRoomInfo();
        this.price = hall.getPrice();

        this.addressLine1 = hall.getAddressLine1();
        this.addressLine2 = hall.getAddressLine2();
        this.city = hall.getCity();
        this.state = hall.getState();
        this.postalCode = hall.getPostalCode();
        this.country = hall.getCountry();

        this.description = hall.getDescription();
        this.status = hall.getStatus();

        this.imagePaths = hall.getImagePaths(); // this returns List<String> from comma-separated string
        this.virtualTourMap = hall.getVirtualTourMap();
        this.mapEmbedUrl = hall.getMapEmbedUrl();
        this.featureBannerImage = hall.getFeatureBannerImage();
        this.videoSrc = hall.getVideoSrc();

        this.amenities = hall.getAmenities();
        this.averageRating = hall.getAverageRating();
    }

    // --- Getters and Setters ---
    public Long getHallId() {
        return hallId;
    }

    public void setHallId(Long hallId) {
        this.hallId = hallId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public Integer getTotalRooms() {
        return totalRooms;
    }

    public void setTotalRooms(Integer totalRooms) {
        this.totalRooms = totalRooms;
    }

    public BigDecimal getRoomPrice() {
        return roomPrice;
    }

    public void setRoomPrice(BigDecimal roomPrice) {
        this.roomPrice = roomPrice;
    }

    public String getRoomInfo() {
        return roomInfo;
    }

    public void setRoomInfo(String roomInfo) {
        this.roomInfo = roomInfo;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public List<String> getImagePaths() {
        return imagePaths;
    }

    public void setImagePaths(List<String> imagePaths) {
        this.imagePaths = imagePaths;
    }

    public String getVirtualTourMap() {
        return virtualTourMap;
    }

    public void setVirtualTourMap(String virtualTourMap) {
        this.virtualTourMap = virtualTourMap;
    }

    public String getMapEmbedUrl() {
        return mapEmbedUrl;
    }

    public void setMapEmbedUrl(String mapEmbedUrl) {
        this.mapEmbedUrl = mapEmbedUrl;
    }

    public String getFeatureBannerImage() {
        return featureBannerImage;
    }

    public void setFeatureBannerImage(String featureBannerImage) {
        this.featureBannerImage = featureBannerImage;
    }

    public String getVideoSrc() {
        return videoSrc;
    }

    public void setVideoSrc(String videoSrc) {
        this.videoSrc = videoSrc;
    }

    public List<String> getAmenities() {
        return amenities;
    }

    public void setAmenities(List<String> amenities) {
        this.amenities = amenities;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }
}
