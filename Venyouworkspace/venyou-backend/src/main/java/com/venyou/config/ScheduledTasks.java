package com.venyou.config;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.venyou.service.BookingService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ScheduledTasks {

    private final BookingService hallBookingService;

    // Run every day at midnight
    @Scheduled(cron = "0 0 0 * * ?")
    public void processDuePayments() {
        hallBookingService.checkAndProcessDuePayments();
    }
}