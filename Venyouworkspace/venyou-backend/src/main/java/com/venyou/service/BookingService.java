package com.venyou.service;

import java.math.BigDecimal;

public interface BookingService {
    String bookHall(Long hallId, Long userId, String eventDate, String eventEndDate,
                    String eventStartTime, String eventEndTime, String paymentType, BigDecimal advanceAmount);
}