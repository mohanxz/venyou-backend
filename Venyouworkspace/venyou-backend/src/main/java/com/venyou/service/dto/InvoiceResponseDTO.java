package com.venyou.service.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class InvoiceResponseDTO {

    private Long invoiceId;
    private String uniqueInvoiceNumber;
    private BigDecimal totalAmount;
    private LocalDateTime issuedDate;
}
