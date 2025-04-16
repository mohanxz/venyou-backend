package com.venyou.service.impl;

import com.venyou.model.*;
import com.venyou.repository.*;
import com.venyou.service.BookingService;
import com.venyou.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class BookingServiceImpl implements BookingService {

    @Autowired
    private HallRepository hallRepository;
    @Autowired
    private HallAvailabilityRepository hallAvailabilityRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private InvoiceRepository invoiceRepository;
    @Autowired
    private BookingAdvanceDetailsRepository bookingAdvanceDetailsRepository;
    @Autowired
    private NotificationService notificationService;

    @Transactional
    public String bookHall(Long hallId, Long userId, String eventDate, String eventEndDate,
                           String eventStartTime, String eventEndTime, String paymentType, BigDecimal advanceAmount) {
        // Parse String inputs to correct types
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalDate parsedEventDate = LocalDate.parse(eventDate, dateFormatter);
        LocalDate parsedEventEndDate = eventEndDate != null && !eventEndDate.isEmpty() 
                ? LocalDate.parse(eventEndDate, dateFormatter) 
                : parsedEventDate; // Default to eventDate if null or empty
        LocalTime parsedStartTime = LocalTime.parse(eventStartTime, timeFormatter);
        LocalTime parsedEndTime = LocalTime.parse(eventEndTime, timeFormatter);

        // Fetch hall and user
        Hall hall = hallRepository.findById(hallId)
                .orElseThrow(() -> new RuntimeException("Hall not found with ID: " + hallId));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        // Check availability for all days
        List<LocalDate> unavailableDates = new ArrayList<>();
        List<LocalDate> allDates = new ArrayList<>();
        LocalDate currentDate = parsedEventDate;

        while (!currentDate.isAfter(parsedEventEndDate)) {
            allDates.add(currentDate);
            List<HallAvailability> overlappingSlots = hallAvailabilityRepository.findOverlappingBookedSlots(
                    hallId, currentDate, parsedStartTime, parsedEndTime);
            if (!overlappingSlots.isEmpty()) {
                unavailableDates.add(currentDate);
            }
            currentDate = currentDate.plusDays(1);
        }

        // If any day is unavailable, return feedback
        if (!unavailableDates.isEmpty()) {
            List<LocalDate> availableDates = allDates.stream()
                    .filter(date -> !unavailableDates.contains(date))
                    .toList();
            StringBuilder message = new StringBuilder("Hall unavailable on: ");
            message.append(unavailableDates.stream()
                    .map(LocalDate::toString)
                    .reduce((a, b) -> a + ", " + b).orElse(""));
            if (!availableDates.isEmpty()) {
                message.append(". Available on: ");
                message.append(availableDates.stream()
                        .map(LocalDate::toString)
                        .reduce((a, b) -> a + ", " + b).orElse(""));
            }
            message.append(". Please adjust your dates.");
            return message.toString();
        }

        // All days are available—proceed with booking
        int numberOfDays = (int) (parsedEventEndDate.toEpochDay() - parsedEventDate.toEpochDay() + 1);
        BigDecimal totalPrice = hall.getPrice().multiply(BigDecimal.valueOf(numberOfDays));

        // Create and save Booking first
        Booking booking = new Booking();
        booking.setUser(user);
        booking.setHall(hall);
        booking.setEventDate(parsedEventDate);
        booking.setEventEndDate(parsedEventEndDate);
        booking.setEventStartTime(parsedStartTime);
        booking.setEventEndTime(parsedEndTime);
        booking.setTotalPrice(totalPrice);
        booking.setStatus(Booking.Status.PENDING); // Initially PENDING until payment succeeds

        // Save booking to get an ID
        bookingRepository.save(booking); // This assigns bookingId

        // Handle payment and update booking status
        if ("FULL_PAYMENT".equalsIgnoreCase(paymentType)) {
            booking.setBookingType(Booking.BookingType.FULL_PAYMENT);
            booking.setAmountPaid(totalPrice);

            Transaction transaction = new Transaction();
            transaction.setUser(user);
            transaction.setBooking(booking);
            transaction.setAmount(totalPrice);
            transaction.setPaymentType(Transaction.PaymentType.CARD); // Default for now
            transaction.setPaymentStatus(Transaction.PaymentStatus.COMPLETED);
            transactionRepository.save(transaction);

            Invoice invoice = new Invoice();
            invoice.setUser(user);
            invoice.setBooking(booking);
            invoice.setTotalAmount(totalPrice);
            invoice.setUniqueInvoiceNumber("INV-" + System.currentTimeMillis());
            invoiceRepository.save(invoice);

            // Payment succeeded—update booking status
            booking.setStatus(Booking.Status.CONFIRMED);
            bookingRepository.save(booking);
        } else if ("ADVANCE_PAYMENT".equalsIgnoreCase(paymentType)) {
            if (advanceAmount == null || advanceAmount.compareTo(BigDecimal.ZERO) <= 0 || advanceAmount.compareTo(totalPrice) >= 0) {
                throw new RuntimeException("Invalid advance amount: " + advanceAmount);
            }
            booking.setBookingType(Booking.BookingType.ADVANCE_PAYMENT);
            booking.setAmountPaid(advanceAmount);

            BookingAdvanceDetails advanceDetails = new BookingAdvanceDetails();
            advanceDetails.setBooking(booking);
            advanceDetails.setAdvanceAmount(advanceAmount);
            advanceDetails.setRemainingAmount(totalPrice.subtract(advanceAmount));
            advanceDetails.setFullPaymentDueDate(parsedEventDate.minusDays(1)); // Example: Due day before event
            advanceDetails.setAdvancePaymentDate(LocalDateTime.now()); // Set
            bookingAdvanceDetailsRepository.save(advanceDetails);

            // Payment succeeded—update booking status
            booking.setStatus(Booking.Status.CONFIRMED);
            bookingRepository.save(booking);
        } else {
            throw new RuntimeException("Invalid payment type: " + paymentType);
        }

        // Payment is confirmed—now mark hall as BOOKED
        List<HallAvailability> availabilitySlots = new ArrayList<>();
        currentDate = parsedEventDate;
        while (!currentDate.isAfter(parsedEventEndDate)) {
            HallAvailability availability = new HallAvailability();
            availability.setHall(hall);
            availability.setDate(currentDate);
            availability.setStartTime(parsedStartTime);
            availability.setEndTime(parsedEndTime);
            availability.setStatus(HallAvailability.Status.BOOKED);
            availabilitySlots.add(availability);
            currentDate = currentDate.plusDays(1);
        }
        hallAvailabilityRepository.saveAll(availabilitySlots);

        // Notify admins
        String notifyMessage = "Hall " + hall.getName() + " booked by " + user.getName() + " for " + eventDate + 
                (parsedEventEndDate.equals(parsedEventDate) ? "" : " to " + eventEndDate) + 
                " " + eventStartTime + "-" + eventEndTime;
        notificationService.notifyAdmins(notifyMessage);

        // Return confirmation
        if ("FULL_PAYMENT".equals(paymentType)) {
            return "Booking confirmed! Booking ID: " + booking.getBookingId() + ", Hall: " + hall.getName() +
                    ", Date: " + eventDate + (parsedEventEndDate.equals(parsedEventDate) ? "" : " to " + eventEndDate) +
                    ", Time: " + eventStartTime + "-" + eventEndTime + ", Total: " + totalPrice;
        } else {
            return "Advance booking confirmed! Booking ID: " + booking.getBookingId() + ", Hall: " + hall.getName() +
                    ", Date: " + eventDate + (parsedEventEndDate.equals(parsedEventDate) ? "" : " to " + eventEndDate) +
                    ", Time: " + eventStartTime + "-" + eventEndTime + ", Paid: " + advanceAmount + ", Remaining: " +
                    totalPrice.subtract(advanceAmount);
        }
    }
}