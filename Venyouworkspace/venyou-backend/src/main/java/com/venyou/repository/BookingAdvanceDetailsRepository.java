package com.venyou.repository;

import java.time.LocalDate;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.venyou.model.BookingAdvanceDetails.PaymentStatus;
import com.venyou.model.BookingAdvanceDetails;

@Repository
public interface BookingAdvanceDetailsRepository extends JpaRepository<BookingAdvanceDetails, Long> {

         @Modifying
    @Query("UPDATE BookingAdvanceDetails bad SET bad.finalPaymentStatus = :status WHERE bad.booking.bookingId = :bookingId")
    void updatePaymentStatus(@Param("bookingId") Long bookingId, @Param("status") PaymentStatus status);
    
    List<BookingAdvanceDetails> findByFullPaymentDueDateBeforeAndFinalPaymentStatus(LocalDate date, PaymentStatus status);
}
