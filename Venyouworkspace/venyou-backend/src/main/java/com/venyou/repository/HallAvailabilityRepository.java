package com.venyou.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.venyou.model.HallAvailability;

@Repository
public interface HallAvailabilityRepository extends JpaRepository<HallAvailability, Long> {
    // Custom query to check for overlapping booked slots
    @Query("SELECT ha FROM HallAvailability ha WHERE ha.hall.hallId = :hallId AND ha.date = :date AND ha.status = 'BOOKED' AND (ha.startTime < :endTime AND ha.endTime > :startTime)")
    List<HallAvailability> findOverlappingBookedSlots(
            @Param("hallId") Long hallId,
            @Param("date") LocalDate date,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime
    );

}
