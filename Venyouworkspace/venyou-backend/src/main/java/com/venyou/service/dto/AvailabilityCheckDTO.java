
package com.venyou.service.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.Data;

@Data
public class AvailabilityCheckDTO {
    @NotNull(message = "hallId cannot be null")
    private Long hallId;

    private String eventType;

    @NotNull(message = "startDate cannot be null")
    @Future(message = "startDate must be in the future")
    private LocalDate startDate;

    @NotNull(message = "endDate cannot be null")
    @Future(message = "endDate must be in the future")
    private LocalDate endDate;

    @NotNull(message = "startTime cannot be null")
    private LocalTime startTime;

    @NotNull(message = "endTime cannot be null")
    private LocalTime endTime;

    private Long categoryId;
}
