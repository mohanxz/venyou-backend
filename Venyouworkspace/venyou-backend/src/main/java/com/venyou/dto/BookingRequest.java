package com.venyou.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class BookingRequest {
    private Long hallId;
    private Long userId;
    private String eventDate;      // e.g., "2025-04-09"
    private String eventEndDate;   // e.g., "2025-04-10" (new field)
    private String eventStartTime; // e.g., "13:00"
    private String eventEndTime;   // e.g., "16:00"
    private String paymentType;    // "FULL_PAYMENT" or "ADVANCE_PAYMENT"
    private BigDecimal advanceAmount; // Null for full payment
}