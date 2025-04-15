package com.venyou.service.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import com.venyou.model.Booking.BookingType;

import lombok.Data;

@Data
public class BookingRequestDTO {
    private Long hallId;
    private Long userId;
    private String eventType;
    private LocalDate eventStartDate;
    private LocalDate eventEndDate;
    private LocalTime eventStartTime;
    private LocalTime eventEndTime;
    private BigDecimal totalPrice;
    private BookingType bookingType;
    private String additionalInput;
}