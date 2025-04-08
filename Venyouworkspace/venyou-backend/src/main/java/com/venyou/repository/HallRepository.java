package com.venyou.repository;

import com.venyou.model.Hall;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface HallRepository extends JpaRepository<Hall, Long> {

    // Fetch halls by owner ID
    List<Hall> findByOwnerOwnerId(Long ownerId);

    // Fetch halls along with their owner and category (optimized)
    @Query("SELECT h FROM Hall h JOIN FETCH h.owner JOIN FETCH h.category")
    List<Hall> findAllWithOwnerAndCategory();

    // Fetch a single hall with owner and category
    @Query("SELECT h FROM Hall h JOIN FETCH h.owner JOIN FETCH h.category WHERE h.hallId = :hallId")
    Optional<Hall> findByIdWithOwnerAndCategory(@Param("hallId") Long hallId);

    // Fetch halls by brand ID
    @Query("SELECT h FROM Hall h WHERE h.brand.brandId = :brandId")
    List<Hall> findByBrandBrandId(@Param("brandId") Long brandId);

    // Fetch halls with pagination
    @Query("SELECT h FROM Hall h")
    Page<Hall> findAllHalls(Pageable pageable);
}
