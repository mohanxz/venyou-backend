package com.venyou.service.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import com.venyou.model.BookingAdvanceDetails;
import lombok.Data;

@Data
public class BookingAdvanceDetailsDTO {

    private Long bookingAdvanceId;
    private BigDecimal advanceAmount;
    private BigDecimal remainingAmount;
    private LocalDate fullPaymentDueDate;
    private LocalDateTime advancePaymentDate;
    private LocalDateTime fullPaymentDate;
    private BookingAdvanceDetails.PaymentStatus finalPaymentStatus;
}