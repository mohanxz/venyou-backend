package com.venyou.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.*;

@Entity
@Table(
    name = "halls",
    indexes = {
        @Index(name = "idx_hall_owner", columnList = "owner_id"),
        @Index(name = "idx_hall_category", columnList = "category_id"),
        @Index(name = "idx_hall_brand", columnList = "brand_id")
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Hall {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long hallId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    @JsonIgnoreProperties({"halls"})
    private Owner owner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id")
    @JsonIgnoreProperties({"halls"})
    private Brand brand;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    @JsonIgnoreProperties({"halls"})
    private HallCategory category;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(nullable = false)
    private Integer capacity;

    private Integer totalRooms;

    @Column(precision = 15, scale = 2)
    private BigDecimal roomPrice;

    private String roomInfo;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal price;

    @Column(length = 255)
    private String addressLine1;

    @Column(length = 255)
    private String addressLine2;

    @Column(length = 100)
    private String city;

    @Column(length = 100)
    private String state;

    @Column(length = 20)
    private String postalCode;

    @Column(length = 100)
    private String country;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Status status = Status.AVAILABLE;

    @Lob
    @Column(columnDefinition = "TEXT")
    @JsonIgnore
    private String imagePaths; // Stored as comma-separated string in DB

    @Lob
    @Column(columnDefinition = "TEXT")
    private String virtualTourMap;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String featureBannerImage;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String videoSrc;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "hall_amenities", joinColumns = @JoinColumn(name = "hall_id"))
    @Column(name = "amenity")
    private List<String> amenities;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Transient
    private double averageRating;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String mapEmbedUrl;

    public enum Status {
        AVAILABLE, BOOKED
    }

    // Custom Getter/Setter for imagePaths to handle List in JSON

    @JsonProperty("imagePaths")
    public List<String> getImagePaths() {
        return (imagePaths != null && !imagePaths.trim().isEmpty())
                ? Arrays.asList(imagePaths.split(","))
                : List.of();
    }

    @JsonProperty("imagePaths")
    public void setImagePaths(List<String> images) {
        this.imagePaths = (images != null && !images.isEmpty())
                ? images.stream().map(String::trim).collect(Collectors.joining(","))
                : null;
    }

    public void updateRating(List<Review> reviews) {
        this.averageRating = (reviews == null || reviews.isEmpty())
                ? 0.0
                : reviews.stream().mapToInt(Review::getRating).average().orElse(0.0);
    }
}
