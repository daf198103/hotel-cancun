package com.hotel.booking.service.impl;

import com.hotel.booking.dto.BookingDTORequest;
import com.hotel.booking.dto.BookingDTOResponse;
import com.hotel.booking.model.Booking;
import com.hotel.booking.repository.BookingRepository;
import com.hotel.booking.utils.BookingConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class BookingService implements com.hotel.booking.service.BookingService {


    private static final Logger LOGGER = LoggerFactory.getLogger(BookingService.class);

    private final BookingRepository bookingRepository;

    private static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuu-MM-dd");


    @Autowired
    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }


    @Override
    public BookingDTOResponse save(BookingDTORequest bookingDTORequest) {
        if(validateBookingRequest(bookingDTORequest)) {
            LOGGER.info("Booking validated. \n {} ", bookingDTORequest);
            final Booking booking = bookingRepository.save(parseDTOToEntity(bookingDTORequest));
            return parseEntityToDTOResponse(booking);
        } else {
            return new BookingDTOResponse();
        }

    }

    @Override
    public BookingDTOResponse findById(Long id) {
        Optional<Booking> booking = bookingRepository.findById(id);
        if (!booking.isPresent()) {
           LOGGER.error("Booking with id - {} WAS NOT FOUND!",id);
           return new BookingDTOResponse();
        }
        LOGGER.info("Booking id {} is present", id);
        return parseEntityToDTOResponse(Optional.of(booking).get().orElse(new Booking()));
    }

    @Override
    public List<BookingDTOResponse> findAll() {
        List<Booking> listOfBooking = bookingRepository.findAll();
        if(listOfBooking.isEmpty()){
            LOGGER.error("No Bookings to Return!");
            return new ArrayList<>();
        } else {
            LOGGER.info("Bookings found {} ", listOfBooking);
            return listOfBooking.stream().map(BookingService::parseEntityToDTOResponse)
                    .collect(Collectors.toList());
        }

    }

    @Override
    public void deleteById(Long id) {
        final Optional<Booking> booking = bookingRepository.findById(id);
        if (!booking.isPresent()) {
            LOGGER.error("Booking with idCard - {} WAS NOT FOUND!",id);
        } else {
            bookingRepository.delete(booking.get());
        }
    }

    /**
     * Logical deletion
     * @param id sets expired to true
     * just to keep tracking of DB
     * */
    @Override
    public void deleteLogicallyById(Long id) {
        final Optional<Booking> booking = bookingRepository.findById(id);
        if (!booking.isPresent()) {
            LOGGER.error("Booking with id - {} WAS NOT FOUND!",id);
        } else {
            bookingRepository.deleteLogicallyById(id);
        }
    }

    @Override
    public void update(Long id, BookingDTORequest bookingDTORequest) {
        final Optional<Booking> booking = bookingRepository.findById(id);
        if (!booking.isPresent()) {
            LOGGER.error("Booking with idCard - {} WAS NOT FOUND!",id);
        } else {
            LOGGER.info("Booking {} update validated.", bookingDTORequest);
            bookingRepository.save(parseDTOUpdatingDate(booking.get(), bookingDTORequest));
        }
    }


    /**
     * Validates the booking request according to Business Logic.
     *
     * @param bookingDTORequest The request to be validated.
     */
    private boolean validateBookingRequest(final BookingDTORequest bookingDTORequest) {
        if (LocalDate.parse(bookingDTORequest.getStartDate(),dtf).isAfter(LocalDate.parse(bookingDTORequest.getFinishDate(),dtf))) {
            LOGGER.info("The start date parameter should be before the finish date parameter.");
            return false;
        }

        if (LocalDate.parse(bookingDTORequest.getStartDate(),dtf).equals(LocalDate.now()) || LocalDate.parse(bookingDTORequest.getStartDate(),dtf).isBefore(LocalDate.now())) {
            LOGGER.info("The reservation must start at least the next day of booking");
            return false;
        }

        long diffInDays = ChronoUnit.DAYS.between(LocalDate.parse(bookingDTORequest.getStartDate(),dtf), LocalDate.parse(bookingDTORequest.getFinishDate(),dtf));
        if (diffInDays > BookingConstants.MAXIMUM_DIFF_STAY_DAYS) {
            LOGGER.info("The stay can't be longer than " + (BookingConstants.MAXIMUM_DIFF_STAY_DAYS + 1) + " days.");
            return false;
        }

        diffInDays = ChronoUnit.DAYS.between(LocalDate.now(), LocalDate.parse(bookingDTORequest.getStartDate(),dtf));
        if (diffInDays > BookingConstants.MAXIMUM_DIFF_DAYS_IN_ADVANCE) {
            LOGGER.info("The reservation can't be made more than " + BookingConstants.MAXIMUM_DIFF_DAYS_IN_ADVANCE + " days in advance.");
            return false;
        }

        if (!availabilityDays(bookingDTORequest)) {
            LOGGER.info("There is already a reservation on this date. Choose another date, please");
            return false;
        }
        return true;
    }

        /**
         * Validates if the days from the request are available , room not already booked..
         *
         * @param bookingDTORequest The request to be validated.
         * @return <b>true</b> if the days are available, <b>false</b> otherwise.
         */
        private boolean availabilityDays(final BookingDTORequest bookingDTORequest) {

            final List<BookingDTOResponse> bookingDTOresponses = findAll();
            final LocalDate requestStartDate = LocalDate.parse(bookingDTORequest.getStartDate(), dtf);
            final LocalDate requestFinishDate = LocalDate.parse(bookingDTORequest.getFinishDate(),dtf);

            LocalDate responseStartDate;
            LocalDate responseFinishDate;
            if(!bookingDTOresponses.isEmpty()) {
                for (BookingDTOResponse response : bookingDTOresponses) {
                    responseStartDate = LocalDate.parse(response.getStartDate(), dtf);
                    responseFinishDate = LocalDate.parse(response.getFinishDate(), dtf);

                    // If "startDate" or "finishDate" from the request is equals to "startDate" or "finishDate" from an existing booking, not available.
                    if (requestStartDate.isEqual(responseStartDate) ||
                            requestStartDate.isEqual(responseFinishDate) ||
                            requestFinishDate.isEqual(responseStartDate) ||
                            requestFinishDate.isEqual(responseFinishDate)) {
                        return false;
                    }

                    // If "startDate" or "finishDate" from the request is in between "startDate" or "finishDate" from an existing booking, not available.
                    if ((requestStartDate.isAfter(responseStartDate) &&
                            requestStartDate.isBefore(responseFinishDate)) ||
                            (requestFinishDate.isAfter(responseStartDate) &&
                                    requestFinishDate.isBefore(responseFinishDate))) {
                        return false;
                    }

                    // If "startDate" and "finishDate" from the response are in between the "startDate" and "finishDate" from the request, not available.
                    if (responseStartDate.isAfter(requestStartDate) &&
                            responseStartDate.isBefore(requestFinishDate) &&
                            responseFinishDate.isAfter(requestStartDate) &&
                            responseFinishDate.isBefore(requestFinishDate)) {
                        return false;
                    }
                }
            }
            return true;
        }

    public static Booking parseDTOToEntity(BookingDTORequest bookingDTORequest) {
        Booking booking = new Booking();
        booking.setIdCard(bookingDTORequest.getIdCard());
        booking.setName(bookingDTORequest.getName());
        booking.setStartDate(LocalDate.parse(bookingDTORequest.getStartDate(),dtf));
        booking.setFinishDate(LocalDate.parse(bookingDTORequest.getFinishDate(),dtf));
        booking.setCreatedDate(LocalDate.now(ZoneOffset.UTC));
        booking.setUpdatedDate(LocalDate.now(ZoneOffset.UTC));
        booking.setExpired(false);
        return booking;
    }

    public static Booking parseDTOUpdatingDate(final Booking booking,
                                                       final BookingDTORequest bookingDTORequest) {
            booking.setName(bookingDTORequest.getName());
            booking.setIdCard(booking.getIdCard());
            booking.setStartDate(LocalDate.parse(bookingDTORequest.getStartDate(),dtf));
            booking.setFinishDate(LocalDate.parse(bookingDTORequest.getFinishDate(),dtf));
            booking.setUpdatedDate(LocalDate.now(ZoneOffset.UTC));
        return booking;
    }

    public static BookingDTOResponse parseEntityToDTOResponse(Booking booking) {
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("uuuu-MM-dd");
            return new BookingDTOResponse(
                    booking.getId(),
                    booking.getIdCard(),
                    booking.getName(),
                    booking.getStartDate().format(dateFormat),
                    booking.getFinishDate().format(dateFormat),
                    booking.isExpired());

    }


}
