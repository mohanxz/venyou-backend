package com.venyou.controller;

import com.venyou.dto.BookingRequest;
import com.venyou.dto.BookingResponse;
import com.venyou.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @PostMapping("/book")
    public ResponseEntity<BookingResponse> bookHall(@RequestBody BookingRequest request) {
        String result = bookingService.bookHall(
                request.getHallId(),
                request.getUserId(),
                request.getEventDate(),
                request.getEventEndDate(),
                request.getEventStartTime(),
                request.getEventEndTime(),
                request.getPaymentType(),
                request.getAdvanceAmount()
        );
        BookingResponse response = new BookingResponse();
        response.setMessage(result);
        return ResponseEntity.ok(response);
    }
}

/* i am ami*/