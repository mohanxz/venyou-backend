package com.venyou.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class HallAvailabilityDTO {
    private Long availabilityId;
    private Long hallId;
    private String hallName;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private String status;
}