package com.venyou.service.dto;

import lombok.Data;

@Data
public class PriceCalculationRequest {
    private Long hallId;
    private String startDateTime;
    private String endDateTime;
}
