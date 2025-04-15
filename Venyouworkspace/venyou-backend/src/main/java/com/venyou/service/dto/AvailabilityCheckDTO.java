package com.venyou.service.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Data;

@Data
public class AvailabilityCheckDTO {
    private Long hallId;
    private String eventType;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private long categoryId;
} 