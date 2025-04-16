package com.venyou.service.dto;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class PriceCalculationResponse {
    private BigDecimal basePrice;
    private BigDecimal additionalHoursPrice;
    private BigDecimal subtotal;
}
