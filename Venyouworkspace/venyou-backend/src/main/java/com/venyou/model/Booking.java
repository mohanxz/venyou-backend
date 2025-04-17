package com.venyou.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "bookings")
@Getter
@Setter
@NoArgsConstructor
public class Booking {

    public enum Status {
        PENDING,
        ADVANCE_PAID,
        CONFIRMED,
        FAILED,
        CANCELLED,
        PAYMENT_PENDING
    }

    public enum BookingType {
        FULL_PAYMENT,
        ADVANCE_PAYMENT
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookingId;

    @Enumerated(EnumType.STRING)
    private BookingType bookingType;

    private String eventType;

    private LocalDate eventStartDate;
    private LocalDate eventEndDate;

    private LocalTime eventStartTime;
    private LocalTime eventEndTime;

    private LocalTime bufferStartTime;
    private LocalTime bufferEndTime;

    private LocalDateTime bookingDate;

    private BigDecimal totalPrice;

    private BigDecimal amountPaid;

    @Enumerated(EnumType.STRING)
    private Status status;

    private String additionalInput;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hall_id")
    private Hall hall;

    @OneToOne(mappedBy = "booking", cascade = CascadeType.ALL, orphanRemoval = true)
    private BookingAdvanceDetails bookingAdvanceDetails;

    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Transaction> transactions = new ArrayList<>();

    @OneToOne(mappedBy = "booking", cascade = CascadeType.ALL, orphanRemoval = true)
    private Invoice invoice;

    @OneToOne(mappedBy = "booking", cascade = CascadeType.ALL, orphanRemoval = true)
    private Cancellation cancellation;
}