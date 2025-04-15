package com.venyou.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.venyou.exception.ResourceNotFoundException;
import com.venyou.model.Booking;
import com.venyou.model.Hall;
import com.venyou.service.BookingService;
import com.venyou.service.HallService;
import com.venyou.service.dto.AvailabilityCheckDTO;
import com.venyou.service.dto.AvailabilityResponseDTO;
import com.venyou.service.dto.BookingRequestDTO;
import com.venyou.service.dto.BookingResponseDTO;
import com.venyou.service.dto.PaymentRequestDTO;
import com.venyou.service.dto.PriceCalculationRequest;
import com.venyou.service.dto.PriceCalculationResponse;
import com.venyou.service.dto.ServiceDTO;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bookings")
public class BookingController {

    @Autowired 
    private BookingService bookingService;

    @Autowired 
    private HallService hallService;

    @PostMapping("/check-availability")
    public ResponseEntity<AvailabilityResponseDTO> checkAvailability(@RequestBody AvailabilityCheckDTO availabilityCheckDTO) {
        return ResponseEntity.ok(bookingService.checkAvailability(availabilityCheckDTO));
    }

    @PostMapping
    public ResponseEntity<BookingResponseDTO> createBooking(
        @RequestBody BookingRequestDTO bookingRequestDTO) {
        BookingResponseDTO response = bookingService.createBooking(bookingRequestDTO);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{hallId}/calculate-price")
public ResponseEntity<PriceCalculationResponse> calculatePrice(
    @PathVariable Long hallId,
    @RequestBody PriceCalculationRequest request) {
    
    return ResponseEntity.ok(bookingService.calculatePrice(hallId, request));
}

    @GetMapping("/process-due-payments")
    public ResponseEntity<String> processDuePayments() {
        bookingService.checkAndProcessDuePayments();
        return ResponseEntity.ok("Due payments processed successfully");
    }

}