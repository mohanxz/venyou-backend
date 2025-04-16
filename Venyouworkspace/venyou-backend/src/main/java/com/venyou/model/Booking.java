package com.venyou.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "bookings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookingId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hall_id", nullable = false)
    private Hall hall;

    @Enumerated(EnumType.STRING)
    private BookingType bookingType = BookingType.FULL_PAYMENT;

    private String eventType;
    private LocalDate eventStartDate;
    private LocalDate eventEndDate;
    private LocalTime eventStartTime;
    private LocalTime eventEndTime;
    
    // Added buffer times
    private LocalTime bufferStartTime;
    private LocalTime bufferEndTime;
    
    private LocalDateTime bookingDate = LocalDateTime.now();
    private BigDecimal totalPrice;
    private BigDecimal amountPaid;

    @Enumerated(EnumType.STRING)
    private Status status = Status.PENDING;

    private String additionalInput;

    @OneToOne(mappedBy = "booking", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private BookingAdvanceDetails bookingAdvanceDetails;

    @OneToOne(mappedBy = "booking", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Cancellation cancellation;

    @OneToOne(mappedBy = "booking", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private RefundRequest refundRequest;

    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Transaction> transactions;
    
    @OneToOne(mappedBy = "booking", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Invoice invoice;
    
    public enum BookingType {
        FULL_PAYMENT, ADVANCE_PAYMENT
    }

    public enum Status {
        PENDING,           // Initial state
        ADVANCE_PAID,      // Advance payment received
        CONFIRMED,         // Full payment received
        ONGOING,          // Event is happening now
        COMPLETED,        // Event completed successfully
        PPEAYMENT_NDING,  // Waiting for full payment
        CANCELLED,        // Booking cancelled
        FAILED            // Payment not completed on time
    }
}

