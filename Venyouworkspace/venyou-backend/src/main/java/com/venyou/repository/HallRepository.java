package com.venyou.repository;

import com.venyou.model.Hall;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
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

    @Query("SELECT h FROM Hall h " +
           "LEFT JOIN h.category c " +
           "LEFT JOIN h.brand b " +
           "WHERE (:name IS NULL OR LOWER(h.name) LIKE LOWER(CONCAT('%', :name, '%'))) " +
           "AND (:city IS NULL OR LOWER(h.city) LIKE LOWER(CONCAT('%', :city, '%'))) " +
           "AND (:state IS NULL OR LOWER(h.state) LIKE LOWER(CONCAT('%', :state, '%'))) " +
           "AND (:address IS NULL OR " +
           "     LOWER(h.addressLine1) LIKE LOWER(CONCAT('%', :address, '%')) OR " +
           "     LOWER(h.addressLine2) LIKE LOWER(CONCAT('%', :address, '%'))) " +
           "AND (:minPrice IS NULL OR h.price >= :minPrice) " +
           "AND (:maxPrice IS NULL OR h.price <= :maxPrice) " +
           "AND (:minCapacity IS NULL OR h.capacity >= :minCapacity) " +
           "AND (:maxCapacity IS NULL OR h.capacity <= :maxCapacity) " +
           "AND (:categoryName IS NULL OR c.categoryName = :categoryName) " +
           "AND (:brandName IS NULL OR LOWER(b.name) LIKE LOWER(CONCAT('%', :brandName, '%'))) " +
           "AND (:startDate IS NULL OR :endDate IS NULL OR " +
           "     NOT EXISTS (" +
           "         SELECT ha FROM HallAvailability ha " +
           "         WHERE ha.hall.hallId = h.hallId " +
           "         AND ha.status = 'BOOKED' " +
           "         AND ha.date BETWEEN :startDate AND :endDate " +
           "         AND (:startTime IS NULL OR :endTime IS NULL OR " +
           "              (ha.startTime < :endTime AND ha.endTime > :startTime))" +
           "     ))")
    List<Hall> findHallsByFilters(
            @Param("name") String name,
            @Param("city") String city,
            @Param("state") String state,
            @Param("address") String address,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            @Param("minCapacity") Integer minCapacity,
            @Param("maxCapacity") Integer maxCapacity,
            @Param("categoryName") String categoryName,
            @Param("brandName") String brandName,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime,
            Pageable pageable
    );
}