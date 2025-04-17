package com.venyou.repository;

import com.venyou.model.HallAvailability;
import com.venyou.model.HallAvailability.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface HallAvailabilityRepository extends JpaRepository<HallAvailability, Long> {

    // Fixed query to check for overlapping booked slots
    @Query("SELECT ha FROM HallAvailability ha " +
           "WHERE ha.hall.hallId = :hallId " +
           "AND ha.status = 'BOOKED' " +
           "AND ha.startDate <= :date " +
           "AND ha.endDate >= :date " +
           "AND (ha.startTime < :endTime AND ha.endTime > :startTime)")
    List<HallAvailability> findOverlappingBookedSlots(
            @Param("hallId") Long hallId,
            @Param("date") LocalDate date,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime
    );

    // Simplified query to find conflicting bookings
    @Query("SELECT ha FROM HallAvailability ha " +
           "WHERE ha.hall.hallId = :hallId " +
           "AND ha.status = 'BOOKED' " +
           "AND ha.startDate <= :endDate " +
           "AND ha.endDate >= :startDate " +
           "AND (:bufferStartTime < ha.endTime AND :bufferEndTime > ha.startTime)")
    List<HallAvailability> findConflictingBookings(
            @Param("hallId") Long hallId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("bufferStartTime") LocalTime bufferStartTime,
            @Param("bufferEndTime") LocalTime bufferEndTime
    );

    List<HallAvailability> findByHallHallIdAndStatus(Long hallId, Status status);

    List<HallAvailability> findByHallHallIdAndStartDateAndEndDate(
            Long hallId,
            LocalDate startDate,
            LocalDate endDate
    );
}