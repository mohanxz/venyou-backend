package com.venyou.service.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ServiceDTO {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private String category;
}
