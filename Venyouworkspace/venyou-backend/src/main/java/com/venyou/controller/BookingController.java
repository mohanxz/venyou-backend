
package com.venyou.controller;

import com.venyou.service.BookingService;
import com.venyou.service.HallService;
import com.venyou.service.dto.AvailabilityCheckDTO;
import com.venyou.service.dto.AvailabilityResponseDTO;
import com.venyou.service.dto.BookingRequestDTO;
import com.venyou.service.dto.BookingResponseDTO;
import com.venyou.service.dto.PriceCalculationRequest;
import com.venyou.service.dto.PriceCalculationResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;
    private final HallService hallService;

    @PostMapping("/check-availability")
    public ResponseEntity<AvailabilityResponseDTO> checkAvailability(@Valid @RequestBody AvailabilityCheckDTO availabilityCheckDTO) {
        return ResponseEntity.ok(bookingService.checkAvailability(availabilityCheckDTO));
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN') and #bookingRequestDTO.userId == authentication.principal.userId")
    public ResponseEntity<BookingResponseDTO> createBooking(@Valid @RequestBody BookingRequestDTO bookingRequestDTO) {
        BookingResponseDTO response = bookingService.createBooking(bookingRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/{hallId}/calculate-price")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<PriceCalculationResponse> calculatePrice(
            @PathVariable Long hallId,
            @Valid @RequestBody PriceCalculationRequest request) {
        return ResponseEntity.ok(bookingService.calculatePrice(hallId, request));
    }

    @GetMapping("/process-due-payments")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<String> processDuePayments() {
        bookingService.checkAndProcessDuePayments();
        return ResponseEntity.ok("Due payments processed successfully");
    }
}