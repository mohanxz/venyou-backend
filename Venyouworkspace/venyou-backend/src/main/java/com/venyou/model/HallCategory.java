package com.venyou.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
    name = "hall_categories",
    indexes = @Index(name = "idx_category_name", columnList = "categoryName")
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HallCategory {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;

    @Column(nullable = false, unique = true, length = 100)
    private String categoryName;
}
