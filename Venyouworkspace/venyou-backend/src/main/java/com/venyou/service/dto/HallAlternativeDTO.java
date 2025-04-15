package com.venyou.service.dto;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HallAlternativeDTO {
    private Long hallId;
    private String hallName;
    private LocalTime availableFrom;
    private LocalTime availableUntil;
    private BigDecimal price;
}