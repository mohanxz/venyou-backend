package com.venyou.model;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table(
    name = "halls",
    indexes = {
        @Index(name = "idx_hall_owner", columnList = "owner_id"),
        @Index(name = "idx_hall_category", columnList = "category_id"),
        @Index(name = "idx_hall_brand", columnList = "brand_id"),
        @Index(name = "idx_hall_status", columnList = "status")
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"owner", "brand", "category", "reviews", "bookings"})
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

    @Column(columnDefinition = "INT DEFAULT 0")
    private Integer totalRooms;

    @Column(precision = 15, scale = 2)
    private BigDecimal roomPrice;

    @Column(length = 255)
    private String roomInfo;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal price;

    @Column(nullable = false, length = 255)
    private String addressLine1;

    @Column(length = 255)
    private String addressLine2;

    @Column(nullable = false, length = 100)
    private String city;

    @Column(nullable = false, length = 100)
    private String state;

    @Column(nullable = false, length = 20)
    private String postalCode;

    @Column(nullable = false, length = 100)
    private String country;

    @Lob
    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private Status status = Status.AVAILABLE;

    @Lob
    @Column(columnDefinition = "TEXT")
    @JsonIgnore
    private String imagePaths;

    @JsonProperty("imagePaths")
    public List<String> getImagePathsAsList() {
        return (imagePaths != null && !imagePaths.trim().isEmpty())
                ? Arrays.stream(imagePaths.split(","))
                        .map(String::trim)
                        .filter(s -> !s.isEmpty())
                        .collect(Collectors.toList())
                : List.of();
    }

    @JsonProperty("imagePaths")
    public void setImagePathsFromList(List<String> images) {
        this.imagePaths = (images != null && !images.isEmpty())
                ? images.stream()
                        .map(String::trim)
                        .filter(s -> !s.isEmpty())
                        .collect(Collectors.joining(","))
                : "";
    }

    @Lob
    @Column(columnDefinition = "TEXT")
    private String virtualTourMap;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String featureBannerImage;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String videoSrc;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
        name = "hall_amenities",
        joinColumns = @JoinColumn(name = "hall_id"),
        indexes = @Index(name = "idx_hall_amenities", columnList = "hall_id")
    )
    @Column(name = "amenity", length = 100)
    @Builder.Default
    private List<String> amenities = new ArrayList<>();

    @Column(nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Transient
    @Builder.Default
    private Double averageRating = 0.0;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String mapEmbedUrl;

    @OneToMany(mappedBy = "hall", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Review> reviews;

    @OneToMany(mappedBy = "hall", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Booking> bookings;

    public enum Status {
        AVAILABLE,
        BOOKED,
        MAINTENANCE,
        INACTIVE,
        UNDER_REVIEW,
        DRAFT
    }

    public void updateAverageRating() {
        if (reviews != null && !reviews.isEmpty()) {
            this.averageRating = reviews.stream()
                    .mapToInt(Review::getRating)
                    .average()
                    .orElse(0.0);
        } else {
            this.averageRating = 0.0;
        }
    }

    public void addAmenity(String amenity) {
        if (amenity != null && !amenity.trim().isEmpty()) {
            List<String> updatedAmenities = new ArrayList<>(Optional.ofNullable(getAmenities()).orElse(List.of()));
            updatedAmenities.add(amenity.trim());
            setAmenities(updatedAmenities.stream().distinct().collect(Collectors.toList()));
        }
    }

    public void removeAmenity(String amenity) {
        if (amenity != null && !amenity.trim().isEmpty()) {
            setAmenities(getAmenities().stream()
                    .filter(a -> !a.equalsIgnoreCase(amenity.trim()))
                    .collect(Collectors.toList()));
        }
    }
}
