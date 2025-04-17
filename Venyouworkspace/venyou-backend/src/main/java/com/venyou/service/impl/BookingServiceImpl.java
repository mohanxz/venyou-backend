
package com.venyou.service.impl;

import com.venyou.service.BookingService;
import com.venyou.service.dto.AvailabilityCheckDTO;
import com.venyou.service.dto.AvailabilityResponseDTO;
import com.venyou.service.dto.BookingAdvanceDetailsDTO;
import com.venyou.service.dto.BookingRequestDTO;
import com.venyou.service.dto.BookingResponseDTO;
import com.venyou.service.dto.InvoiceResponseDTO;
import com.venyou.service.dto.PaymentRequestDTO;
import com.venyou.service.dto.PriceCalculationRequest;
import com.venyou.service.dto.PriceCalculationResponse;
import com.venyou.service.dto.TransactionResponseDTO;
import com.venyou.exception.BookingConflictException;
import com.venyou.exception.InvalidPaymentException;
import com.venyou.exception.ResourceNotFoundException;
import com.venyou.model.Booking;
import com.venyou.model.Booking.BookingType;
import com.venyou.model.Booking.Status;
import com.venyou.model.BookingAdvanceDetails;
import com.venyou.model.Hall;
import com.venyou.model.HallAvailability;
import com.venyou.model.Invoice;
import com.venyou.model.Transaction;
import com.venyou.model.Transaction.PaymentStatus;
import com.venyou.model.Transaction.PaymentType;
import com.venyou.repository.BookingAdvanceDetailsRepository;
import com.venyou.repository.BookingRepository;
import com.venyou.repository.HallAvailabilityRepository;
import com.venyou.repository.HallRepository;
import com.venyou.repository.InvoiceRepository;
import com.venyou.repository.TransactionRepository;
import com.venyou.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class BookingServiceImpl implements BookingService {

    private final HallRepository hallRepository;
    private final UserRepository userRepository;
    private final HallAvailabilityRepository hallAvailabilityRepository;
    private final BookingRepository bookingRepository;
    private final BookingAdvanceDetailsRepository bookingAdvanceDetailsRepository;
    private final TransactionRepository transactionRepository;
    private final InvoiceRepository invoiceRepository;

    @Override
    public AvailabilityResponseDTO checkAvailability(AvailabilityCheckDTO availabilityCheckDTO) {
        Hall hall = hallRepository.findById(availabilityCheckDTO.getHallId())
                .orElseThrow(() -> new ResourceNotFoundException("Hall not found with id: " + availabilityCheckDTO.getHallId()));

        int bufferHours = getBufferHoursForEventType(availabilityCheckDTO.getEventType());
        LocalTime bufferStartTime = availabilityCheckDTO.getStartTime().minusHours(bufferHours);
        LocalTime bufferEndTime = availabilityCheckDTO.getEndTime().plusHours(bufferHours);

        List<HallAvailability> conflicts = hallAvailabilityRepository.findConflictingBookings(
                availabilityCheckDTO.getHallId(),
                availabilityCheckDTO.getStartDate(),
                availabilityCheckDTO.getEndDate(),
                bufferStartTime,
                bufferEndTime);

        AvailabilityResponseDTO response = new AvailabilityResponseDTO();
        response.setHallId(availabilityCheckDTO.getHallId());
        response.setStartDate(availabilityCheckDTO.getStartDate());
        response.setEndDate(availabilityCheckDTO.getEndDate());
        response.setStartTime(availabilityCheckDTO.getStartTime());
        response.setEndTime(availabilityCheckDTO.getEndTime());
        response.setBufferStartTime(bufferStartTime);
        response.setBufferEndTime(bufferEndTime);

        if (conflicts == null || conflicts.isEmpty()) {
            response.setAvailable(true);
            response.setMessage("Hall is available for booking");
        } else {
            response.setAvailable(false);
            response.setMessage("Hall is completely booked for the requested date");
        }
        return response;
    }

    @Override
    @Transactional
    public BookingResponseDTO createBooking(BookingRequestDTO bookingRequestDTO) {
        // Validate input
        validateBookingRequest(bookingRequestDTO);

        // Check availability
        AvailabilityCheckDTO availabilityCheckDTO = mapToAvailabilityCheckDTO(bookingRequestDTO);
        AvailabilityResponseDTO availability = checkAvailability(availabilityCheckDTO);

        if (!availability.isAvailable()) {
            throw new BookingConflictException("Hall is no longer available for the requested dates/times");
        }

        // Calculate buffer times
        int bufferHours = getBufferHoursForEventType(null); // eventType not in DTO, use default
        LocalTime bufferStartTime = bookingRequestDTO.getEventStartTime().minusHours(bufferHours);
        LocalTime bufferEndTime = bookingRequestDTO.getEventEndTime().plusHours(bufferHours);

        // Create the booking
        Booking booking = new Booking();
        booking.setUser(userRepository.findById(bookingRequestDTO.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + bookingRequestDTO.getUserId())));
        booking.setHall(hallRepository.findById(bookingRequestDTO.getHallId())
                .orElseThrow(() -> new ResourceNotFoundException("Hall not found with id: " + bookingRequestDTO.getHallId())));
        booking.setEventStartDate(bookingRequestDTO.getEventDate());
        booking.setEventEndDate(bookingRequestDTO.getEventEndDate());
        booking.setEventStartTime(bookingRequestDTO.getEventStartTime());
        booking.setEventEndTime(bookingRequestDTO.getEventEndTime());
        booking.setBufferStartTime(bufferStartTime);
        booking.setBufferEndTime(bufferEndTime);

        // Calculate totalPrice
        PriceCalculationRequest priceRequest = new PriceCalculationRequest();
        priceRequest.setStartDate(bookingRequestDTO.getEventDate());
        priceRequest.setEndDate(bookingRequestDTO.getEventEndDate());
        priceRequest.setStartTime(bookingRequestDTO.getEventStartTime());
        priceRequest.setEndTime(bookingRequestDTO.getEventEndTime());
        PriceCalculationResponse priceResponse = calculatePrice(bookingRequestDTO.getHallId(), priceRequest);
        booking.setTotalPrice(priceResponse.getSubtotal());
        booking.setBookingType(bookingRequestDTO.getPaymentType() != null &&
                bookingRequestDTO.getPaymentType().equals("ADVANCE_PAYMENT") ? BookingType.ADVANCE_PAYMENT : BookingType.FULL_PAYMENT);

        // Set initial amount paid
        booking.setAmountPaid(BigDecimal.ZERO);
        booking = bookingRepository.save(booking);

        // Create hall availability record
        createHallAvailabilityRecord(booking);

        // Handle payment type specific logic
        if (booking.getBookingType() == BookingType.ADVANCE_PAYMENT) {
            handleAdvancePaymentBooking(booking);
        } else {
            handleFullPaymentBooking(booking);
        }

        // Save and return
        bookingRepository.save(booking);
        return mapToBookingResponseDTO(booking);
    }

    @Override
    @Transactional
    public BookingResponseDTO processPayment(PaymentRequestDTO paymentRequestDTO) {
        if (paymentRequestDTO.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidPaymentException("Payment amount must be positive");
        }

        Booking booking = bookingRepository.findById(paymentRequestDTO.getBookingId())
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + paymentRequestDTO.getBookingId()));

        Transaction transaction = createTransaction(booking, paymentRequestDTO);

        if (booking.getBookingType() == BookingType.ADVANCE_PAYMENT) {
            processAdvancePayment(booking, paymentRequestDTO);
        } else {
            processFullPayment(booking, paymentRequestDTO);
        }

        if (isPaymentComplete(booking)) {
            createInvoice(booking);
            booking.setStatus(Status.CONFIRMED);
        }

        bookingRepository.save(booking);
        Booking updatedBooking = bookingRepository.findByIdWithTransactionsAndInvoice(paymentRequestDTO.getBookingId())
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        return mapToBookingResponseDTO(updatedBooking);
    }

    @Override
    @Transactional
    public void checkAndProcessDuePayments() {
        LocalDate today = LocalDate.now();

        List<BookingAdvanceDetails> duePayments = bookingAdvanceDetailsRepository
                .findByFullPaymentDueDateBeforeAndFinalPaymentStatus(today

, BookingAdvanceDetails.PaymentStatus.PENDING);

        for (BookingAdvanceDetails advanceDetails : duePayments) {
            Booking booking = advanceDetails.getBooking();

            if (booking.getStatus() != Status.ADVANCE_PAID) {
                continue;
            }

            booking.setStatus(Status.FAILED);
            releaseHallAvailability(booking);
            bookingRepository.save(booking);
        }
    }

    private void validateBookingRequest(BookingRequestDTO bookingRequestDTO) {
        if (bookingRequestDTO.getEventDate() == null || bookingRequestDTO.getEventEndDate() == null ||
                bookingRequestDTO.getEventStartTime() == null || bookingRequestDTO.getEventEndTime() == null) {
            throw new IllegalArgumentException("Event dates and times must not be null");
        }

        if (bookingRequestDTO.getEventEndDate().isBefore(bookingRequestDTO.getEventDate())) {
            throw new IllegalArgumentException("Event end date cannot be before start date");
        }

        if (bookingRequestDTO.getEventEndTime().isBefore(bookingRequestDTO.getEventStartTime()) &&
                bookingRequestDTO.getEventDate().equals(bookingRequestDTO.getEventEndDate())) {
            throw new IllegalArgumentException("Event end time cannot be before start time on the same day");
        }

        long daysUntilEvent = ChronoUnit.DAYS.between(LocalDate.now(), bookingRequestDTO.getEventDate());
        if (daysUntilEvent <= 10 && "ADVANCE_PAYMENT".equals(bookingRequestDTO.getPaymentType())) {
            throw new BookingConflictException(
                    "Events within 10 days require full payment. Please change paymentType to FULL_PAYMENT");
        }
    }

    private AvailabilityCheckDTO mapToAvailabilityCheckDTO(BookingRequestDTO bookingRequestDTO) {
        AvailabilityCheckDTO dto = new AvailabilityCheckDTO();
        dto.setHallId(bookingRequestDTO.getHallId());
        dto.setStartDate(bookingRequestDTO.getEventDate());
        dto.setEndDate(bookingRequestDTO.getEventEndDate());
        dto.setStartTime(bookingRequestDTO.getEventStartTime());
        dto.setEndTime(bookingRequestDTO.getEventEndTime());
        return dto;
    }

    private void createHallAvailabilityRecord(Booking booking) {
        HallAvailability hallAvailability = new HallAvailability();
        hallAvailability.setHall(booking.getHall());
        hallAvailability.setStartDate(booking.getEventStartDate());
        hallAvailability.setEndDate(booking.getEventEndDate());
        hallAvailability.setStartTime(booking.getBufferStartTime());
        hallAvailability.setEndTime(booking.getBufferEndTime());
        hallAvailability.setStatus(HallAvailability.Status.BOOKED);
        hallAvailabilityRepository.save(hallAvailability);
    }

    private void handleAdvancePaymentBooking(Booking booking) {
        booking.setStatus(Status.PENDING);

        BookingAdvanceDetails advanceDetails = new BookingAdvanceDetails();
        advanceDetails.setBooking(booking);
        advanceDetails.setAdvanceAmount(booking.getTotalPrice().multiply(new BigDecimal("0.3")));
        advanceDetails.setRemainingAmount(booking.getTotalPrice().subtract(advanceDetails.getAdvanceAmount()));
        advanceDetails.setFullPaymentDueDate(booking.getEventStartDate().minusDays(7));
        advanceDetails.setAdvancePaymentDate(null);
        advanceDetails.setFinalPaymentStatus(BookingAdvanceDetails.PaymentStatus.PENDING);

        booking.setBookingAdvanceDetails(advanceDetails);
        bookingAdvanceDetailsRepository.save(advanceDetails);
    }

    private void handleFullPaymentBooking(Booking booking) {
        booking.setStatus(Status.PAYMENT_PENDING);
    }

    private Transaction createTransaction(Booking booking, PaymentRequestDTO paymentRequestDTO) {
        Transaction transaction = new Transaction();
        transaction.setBooking(booking);
        transaction.setUser(booking.getUser());
        transaction.setAmount(paymentRequestDTO.getAmount());
        transaction.setPaymentType(paymentRequestDTO.getPaymentType());
        transaction.setPaymentStatus(PaymentStatus.COMPLETED);
        return transactionRepository.save(transaction);
    }

    private void processAdvancePayment(Booking booking, PaymentRequestDTO paymentRequestDTO) {
        BookingAdvanceDetails advanceDetails = booking.getBookingAdvanceDetails();

        if (advanceDetails == null) {
            throw new IllegalStateException("Advance details not found for advance payment booking");
        }

        if (advanceDetails.getAdvancePaymentDate() == null) {
            if (paymentRequestDTO.getAmount().compareTo(advanceDetails.getAdvanceAmount()) < 0) {
                throw new InvalidPaymentException("Payment amount is less than the required advance amount");
            }

            advanceDetails.setAdvancePaymentDate(LocalDateTime.now());
            booking.setAmountPaid(paymentRequestDTO.getAmount());
            booking.setStatus(Status.ADVANCE_PAID);
        } else {
            if (paymentRequestDTO.getAmount().compareTo(advanceDetails.getRemainingAmount()) < 0) {
                throw new InvalidPaymentException("Payment amount is less than the remaining amount due");
            }

            advanceDetails.setFullPaymentDate(LocalDateTime.now());
            advanceDetails.setFinalPaymentStatus(BookingAdvanceDetails.PaymentStatus.COMPLETED);
            booking.setAmountPaid(booking.getTotalPrice());
        }

        bookingAdvanceDetailsRepository.save(advanceDetails);
    }

    private void processFullPayment(Booking booking, PaymentRequestDTO paymentRequestDTO) {
        if (paymentRequestDTO.getAmount().compareTo(booking.getTotalPrice()) < 0) {
            throw new InvalidPaymentException("Payment amount is less than the total price");
        }

        booking.setAmountPaid(paymentRequestDTO.getAmount());
    }

    private boolean isPaymentComplete(Booking booking) {
        if (booking.getBookingType() == BookingType.FULL_PAYMENT) {
            return booking.getAmountPaid() != null &&
                    booking.getAmountPaid().compareTo(booking.getTotalPrice()) >= 0;
        } else {
            BookingAdvanceDetails advanceDetails = booking.getBookingAdvanceDetails();
            return advanceDetails != null &&
                    advanceDetails.getFinalPaymentStatus() == BookingAdvanceDetails.PaymentStatus.COMPLETED;
        }
    }

    private void createInvoice(Booking booking) {
        Invoice invoice = new Invoice();
        invoice.setBooking(booking);
        invoice.setUser(booking.getUser());
        invoice.setTotalAmount(booking.getTotalPrice());
        invoice.setUniqueInvoiceNumber(generateInvoiceNumber());

        invoice = invoiceRepository.save(invoice);
        booking.setInvoice(invoice);
        bookingRepository.save(booking);
        invoiceRepository.save(invoice);
    }

    private String generateInvoiceNumber() {
        return "INV-" + LocalDateTime.now().getYear() + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private void releaseHallAvailability(Booking booking) {
        List<HallAvailability> hallAvailabilities = hallAvailabilityRepository
                .findByHallHallIdAndStartDateAndEndDate(
                        booking.getHall().getHallId(),
                        booking.getEventStartDate(),
                        booking.getEventEndDate());

        for (HallAvailability availability : hallAvailabilities) {
            availability.setStatus(HallAvailability.Status.AVAILABLE);
            hallAvailabilityRepository.save(availability);
        }
    }

    private BookingResponseDTO mapToBookingResponseDTO(Booking booking) {
        return BookingResponseDTO.builder()
                .bookingId(booking.getBookingId())
                .bookingType(booking.getBookingType())
                .eventType(booking.getEventType())
                .eventStartDate(booking.getEventStartDate())
                .eventEndDate(booking.getEventEndDate())
                .eventStartTime(booking.getEventStartTime())
                .eventEndTime(booking.getEventEndTime())
                .bookingDate(booking.getBookingDate())
                .totalPrice(booking.getTotalPrice())
                .amountPaid(booking.getAmountPaid())
                .status(booking.getStatus())
                .additionalInput(booking.getAdditionalInput())
                .userId(booking.getUser().getUserId())
                .hallId(booking.getHall().getHallId())
                .hallName(booking.getHall().getName())
                .advanceDetails(booking.getBookingAdvanceDetails() != null ?
                        mapToAdvanceDetailsDTO(booking.getBookingAdvanceDetails()) : null)
                .transactions(booking.getTransactions() != null ?
                        booking.getTransactions().stream()
                                .map(this::mapToTransactionResponseDTO)
                                .toList() : null)
                .invoice(booking.getInvoice() != null ?
                        mapToInvoiceResponseDTO(booking.getInvoice()) : null)
                .build();
    }

    private BookingAdvanceDetailsDTO mapToAdvanceDetailsDTO(BookingAdvanceDetails advanceDetails) {
        BookingAdvanceDetailsDTO dto = new BookingAdvanceDetailsDTO();
        dto.setBookingAdvanceId(advanceDetails.getBookingAdvanceId());
        dto.setAdvanceAmount(advanceDetails.getAdvanceAmount());
        dto.setRemainingAmount(advanceDetails.getRemainingAmount());
        dto.setFullPaymentDueDate(advanceDetails.getFullPaymentDueDate());
        dto.setAdvancePaymentDate(advanceDetails.getAdvancePaymentDate());
        dto.setFullPaymentDate(advanceDetails.getFullPaymentDate());
        dto.setFinalPaymentStatus(advanceDetails.getFinalPaymentStatus());
        return dto;
    }

    private TransactionResponseDTO mapToTransactionResponseDTO(Transaction transaction) {
        TransactionResponseDTO dto = new TransactionResponseDTO();
        dto.setTransactionId(transaction.getTransactionId());
        dto.setAmount(transaction.getAmount());
        dto.setPaymentType(transaction.getPaymentType());
        dto.setPaymentStatus(transaction.getPaymentStatus());
        dto.setTransactionDate(transaction.getTransactionDate());
        dto.setBookingId(transaction.getBooking().getBookingId());
        dto.setUserId(transaction.getUser().getUserId());
        return dto;
    }

    private InvoiceResponseDTO mapToInvoiceResponseDTO(Invoice invoice) {
        InvoiceResponseDTO dto = new InvoiceResponseDTO();
        dto.setInvoiceId(invoice.getInvoiceId());
        dto.setUniqueInvoiceNumber(invoice.getUniqueInvoiceNumber());
        dto.setTotalAmount(invoice.getTotalAmount());
        dto.setIssuedDate(invoice.getIssuedDate());
        return dto;
    }

    private int getBufferHoursForEventType(String eventType) {
        if (eventType == null) return 1;

        switch (eventType.toLowerCase()) {
            case "wedding": return 5;
            case "seminar": return 1;
            case "conference": return 2;
            case "party": return 3;
            default: return 1;
        }
    }

    @Override
    public PriceCalculationResponse calculatePrice(Long hallId, PriceCalculationRequest request) {
        if (request == null || request.getStartDate() == null || request.getEndDate() == null ||
                request.getStartTime() == null || request.getEndTime() == null) {
            throw new IllegalArgumentException("Price calculation request and date-time fields must not be null");
        }

        Hall hall = hallRepository.findById(hallId)
                .orElseThrow(() -> new ResourceNotFoundException("Hall not found with id: " + hallId));

        // Combine date and time into LocalDateTime
        LocalDateTime start;
        LocalDateTime end;
        try {
            start = LocalDateTime.of(request.getStartDate(), request.getStartTime());
            end = LocalDateTime.of(request.getEndDate(), request.getEndTime());
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid date-time values", e);
        }

        long hours = ChronoUnit.HOURS.between(start, end);
        if (hours <= 0) {
            throw new IllegalArgumentException("End time must be after start time");
        }

        BigDecimal basePrice = hall.getPrice() != null
                ? new BigDecimal(hall.getPrice().toString())
                : BigDecimal.ZERO;

        PriceCalculationResponse response = new PriceCalculationResponse();
        response.setBasePrice(basePrice);

        long fullBlocks = hours / 12;
        long remainingHours = hours % 12;

        BigDecimal blockPrice = basePrice.multiply(new BigDecimal(fullBlocks));
        BigDecimal hourlyRate = basePrice.divide(new BigDecimal(12), 2, RoundingMode.HALF_UP);
        BigDecimal remainingPrice = hourlyRate.multiply(new BigDecimal(remainingHours));

        response.setAdditionalHoursPrice(remainingPrice);
        response.setSubtotal(blockPrice.add(remainingPrice));

        return response;
    }

    @Override
    public List<String> getHallAmenities(Long hallId) {
        Hall hall = hallRepository.findById(hallId)
                .orElseThrow(() -> new ResourceNotFoundException("Hall not found"));
        return hall.getAmenities() != null ? hall.getAmenities() : Collections.emptyList();
    }
}
