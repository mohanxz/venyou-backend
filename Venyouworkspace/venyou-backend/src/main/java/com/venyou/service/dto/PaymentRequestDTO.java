package com.venyou.service.dto;
import java.math.BigDecimal;

import com.venyou.model.Transaction.PaymentType;

import lombok.Data;

@Data
public class PaymentRequestDTO {
    private Long bookingId;
    private BigDecimal amount;
    private PaymentType paymentType;
}