package com.venyou.service.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AvailabilityResponseDTO {
    private boolean available;
    private String message;
    private Long hallId;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalDate bufferStartDate;
    private LocalDate bufferEndDate;
    private LocalTime bufferStartTime;
    private LocalTime bufferEndTime;
    private LocalTime nextAvailableTime;
    private List<HallAlternativeDTO> alternativeHalls;
    private String conflictDetails;
}