package com.venyou.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "booking_advance_details")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingAdvanceDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookingAdvanceId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", nullable = false, unique = true)
    private Booking booking;

    private BigDecimal advanceAmount;
    private BigDecimal remainingAmount;
    private LocalDate fullPaymentDueDate;
    private LocalDateTime advancePaymentDate;
    private LocalDateTime fullPaymentDate;

    @Enumerated(EnumType.STRING)
    private PaymentStatus finalPaymentStatus = PaymentStatus.PENDING;

    public enum PaymentStatus {
        PENDING, COMPLETED
    }
}
