package com.venyou.service.dto;

import com.venyou.model.Booking.BookingType;
import com.venyou.model.Booking.Status;

import lombok.Builder;
import lombok.Data;
import java.util.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Builder
public class BookingResponseDTO 
{
    private Long bookingId;
    private BookingType bookingType;
    private String eventType;
    private LocalDate eventStartDate;
    private LocalDate eventEndDate;
    private LocalTime eventStartTime;
    private LocalTime eventEndTime;
    private LocalTime bufferStartTime; 
    private LocalTime bufferEndTime; 
    private LocalDateTime bookingDate;
    private BigDecimal totalPrice;
    private BigDecimal amountPaid;
    private Status status;
    private String additionalInput;
    
    private Long userId;
    private Long hallId;
    private String hallName; 
    private BookingAdvanceDetailsDTO advanceDetails;

    private List<TransactionResponseDTO> transactions;
    private InvoiceResponseDTO invoice;
}

