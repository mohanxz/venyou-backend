
package com.venyou.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookingRequest {
    @NotNull(message = "hallId cannot be null")
    private Long hallId;

    @NotNull(message = "userId cannot be null")
    private Long userId;

    @NotNull(message = "eventDate cannot be null")
    @Future(message = "eventDate must be in the future")
    private LocalDate eventDate;

    @NotNull(message = "eventEndDate cannot be null")
    @Future(message = "eventEndDate must be in the future")
    private LocalDate eventEndDate;

    @NotNull(message = "eventStartTime cannot be null")
    private LocalTime eventStartTime;

    @NotNull(message = "eventEndTime cannot be null")
    private LocalTime eventEndTime;

    private String paymentType; // "FULL_PAYMENT" or "ADVANCE_PAYMENT"

    private BigDecimal advanceAmount; // Null for full payment
}
