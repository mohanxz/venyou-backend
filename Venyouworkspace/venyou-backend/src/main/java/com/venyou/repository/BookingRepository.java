package com.venyou.repository;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.venyou.model.Booking;
import com.venyou.model.Booking.Status;
import java.util.*;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByUserUserId(Long userId);
    
    @Query("SELECT b FROM Booking b WHERE b.hall.hallId = :hallId AND b.status IN :statuses")
    List<Booking> findByHallHallIdAndStatusIn(@Param("hallId") Long hallId, @Param("statuses") List<Status> statuses);
    
    @Query("SELECT b FROM Booking b WHERE b.bookingType = 'ADVANCE_PAYMENT' AND b.status = 'PENDING' AND b.eventStartDate <= :dueDate")
    List<Booking> findPendingAdvanceBookingsWithDueDateBefore(@Param("dueDate") LocalDate dueDate);

    @Query("SELECT b FROM Booking b LEFT JOIN FETCH b.transactions LEFT JOIN FETCH b.invoice WHERE b.bookingId = :bookingId")
    Optional<Booking> findByIdWithTransactionsAndInvoice(@Param("bookingId") Long bookingId);
}
