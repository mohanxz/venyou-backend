package com.venyou.service.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.venyou.model.Transaction.PaymentStatus;
import com.venyou.model.Transaction.PaymentType;
import lombok.Data;

@Data
public class TransactionResponseDTO {

        private Long transactionId;
    private BigDecimal amount;
    private PaymentType paymentType;
    private PaymentStatus paymentStatus;
    private LocalDateTime transactionDate;
    private Long bookingId;
    private Long userId;
}
