
package com.venyou.service.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PriceCalculationRequest {
    @NotNull(message = "startDate cannot be null")
    private LocalDate startDate;

    @NotNull(message = "endDate cannot be null")
    private LocalDate endDate;

    @NotNull(message = "startTime cannot be null")
    private LocalTime startTime;

    @NotNull(message = "endTime cannot be null")
    private LocalTime endTime;

    // Transient fields for backward compatibility (optional, can be removed if not needed)
    public String getStartDateTime() {
        if (startDate == null || startTime == null) {
            return null;
        }
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("h:mm a");
        return startDate.format(dateFormatter) + " " + startTime.format(timeFormatter);
    }

    public String getEndDateTime() {
        if (endDate == null || endTime == null) {
            return null;
        }
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("h:mm a");
        return endDate.format(dateFormatter) + " " + endTime.format(timeFormatter);
    }
}
