package com.venyou.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import com.venyou.model.HallAvailability;
import com.venyou.model.HallAvailability.Status;

@Repository
public interface HallAvailabilityRepository extends JpaRepository<HallAvailability, Long> {

    @Query("SELECT ha FROM HallAvailability ha WHERE " +
    "ha.hall.hallId = :hallId AND " +
    "ha.status = 'BOOKED' AND " +
    "((:startDate BETWEEN ha.startDate AND ha.endDate) OR " +
    "(:endDate BETWEEN ha.startDate AND ha.endDate) OR " +
    "(ha.startDate BETWEEN :startDate AND :endDate) OR " +
    "(ha.endDate BETWEEN :startDate AND :endDate)) AND " +
    "(:bufferStartTime < ha.endTime AND :bufferEndTime > ha.startTime)")
List<HallAvailability> findConflictingBookings(
     @Param("hallId") Long hallId,
     @Param("startDate") LocalDate startDate,
     @Param("endDate") LocalDate endDate,
     @Param("bufferStartTime") LocalTime bufferStartTime,
     @Param("bufferEndTime") LocalTime bufferEndTime);

List<HallAvailability> findByHallHallIdAndStatus(Long hallId, Status status);

List<HallAvailability> findByHallHallIdAndStartDateAndEndDate(
    Long hallId, 
    LocalDate startDate, 
    LocalDate endDate
);
}