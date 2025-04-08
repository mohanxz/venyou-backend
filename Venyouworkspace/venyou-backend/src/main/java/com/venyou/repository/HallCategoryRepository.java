package com.venyou.repository;

import com.venyou.model.HallCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HallCategoryRepository extends JpaRepository<HallCategory, Long> {
    Optional<HallCategory> findByCategoryName(String categoryName);
    boolean existsByCategoryName(String name);

}

