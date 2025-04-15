package com.venyou.service;
import com.venyou.model.Booking;
import com.venyou.service.dto.AvailabilityCheckDTO;
import com.venyou.service.dto.AvailabilityResponseDTO;
import com.venyou.service.dto.BookingRequestDTO;
import com.venyou.service.dto.BookingResponseDTO;
import com.venyou.service.dto.PaymentRequestDTO;
import com.venyou.service.dto.PriceCalculationRequest;
import com.venyou.service.dto.PriceCalculationResponse;
import java.util.*;


public interface BookingService {

    AvailabilityResponseDTO checkAvailability(AvailabilityCheckDTO availabilityCheckDTO);
    BookingResponseDTO createBooking(BookingRequestDTO bookingRequestDTO);
    BookingResponseDTO processPayment(PaymentRequestDTO paymentRequestDTO);
    void checkAndProcessDuePayments();

    PriceCalculationResponse calculatePrice(Long hallId, PriceCalculationRequest request);
List<String> getHallAmenities(Long hallId);
}
