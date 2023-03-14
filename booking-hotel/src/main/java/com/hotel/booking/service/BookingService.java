package com.hotel.booking.service;

import com.hotel.booking.dto.BookingDTORequest;
import com.hotel.booking.dto.BookingDTOResponse;

import java.util.List;

public interface BookingService {

    /**
     * Inserts a booking.
     *
     * @param bookingDTORequest The booking to be inserted.
     * @return The inserted booking.
     */
    BookingDTOResponse save(final BookingDTORequest bookingDTORequest);

    /**
     * Finds a booking by its id.
     *
     * @param id The booking id.
     * @return The booking found.
     */
    BookingDTOResponse findById(final Long id);

    /**
     * Finds all bookings.
     *
     * @return The bookings found.
     */
    List<BookingDTOResponse> findAll();

    /**
     * Deletes a booking by its id.
     *
     * @param id The booking id.
     */
    void deleteById(final Long id);


    /**
     * Updates a booking.
     *
     * @param id The booking id.
     * @param bookingDTORequest The request to update.
     */
    void update(final Long id, final BookingDTORequest bookingDTORequest) throws Exception;
}
