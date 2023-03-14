package com.hotel.booking.service.impl;


import com.hotel.booking.dto.BookingDTORequest;
import com.hotel.booking.dto.BookingDTOResponse;
import com.hotel.booking.model.Booking;
import com.hotel.booking.repository.BookingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class BookingServiceImplTest {


    @InjectMocks
    private BookingServiceImpl bookingService;

    @Mock
    private BookingRepository repository;

    @Test
    public void testFindById() {

        final Booking validBooking = new Booking(1L,"12345","John Connor",
                LocalDate.parse("2023-03-20"),LocalDate.parse("2023-03-22"),
                 LocalDate.parse("2023-03-13"), LocalDate.parse("2023-03-13"),
                false);
        final BookingDTOResponse validResponse = bookingService.parseEntityToDTOResponse(validBooking);

        when(repository.findById(1L)).thenReturn(Optional.of(validBooking));
        final BookingDTOResponse bookingDTOResponse = bookingService.findById(1L);
        assertEquals(validResponse.getStartDate(),bookingDTOResponse.getStartDate());
    }

    @Test
    public void testFindByIdInexistent() {

        final Booking validBooking = new Booking(null,"","",
                LocalDate.now(),LocalDate.now(),
                LocalDate.now(), LocalDate.now(),
                false);

        final BookingDTOResponse validResponse = bookingService.parseEntityToDTOResponse(validBooking);

        when(repository.findById(0L)).thenReturn(Optional.of(validBooking));
        final BookingDTOResponse bookingDTOResponse = bookingService.findById(0L);
        assertEquals(validResponse.getStartDate(),bookingDTOResponse.getStartDate());
    }

    @Test
    public void testFindAllBooking() {
        final Booking validBooking = new Booking(1L,"12345","John Connor",
                LocalDate.parse("2023-03-20"),LocalDate.parse("2023-03-22"),
                LocalDate.parse("2023-03-13"), LocalDate.parse("2023-03-13"),
                false);

        final BookingDTOResponse validResponse = bookingService.parseEntityToDTOResponse(validBooking);
        final List<Booking> expectedBookings = new ArrayList<>();
        expectedBookings.add(validBooking);

        when(repository.findAll()).thenReturn(expectedBookings);

        List<BookingDTOResponse> bookingsDtoResponselist = bookingService.findAll();
        assertEquals(validResponse.getStartDate(), bookingsDtoResponselist.get(0).getStartDate());
        assertEquals(validResponse.getFinishDate(), bookingsDtoResponselist.get(0).getFinishDate());
    }

    @Test()
    public void saveAValidBooking() {
        final BookingDTORequest bookingDTORequest = new BookingDTORequest(
                "12345",
                "John Connor",
                "2023-03-20",
                "2023-03-22");

        final Booking validBooking = new Booking(
                1L,
                "12345",
                "John Connor",
                LocalDate.parse("2023-03-20"),
                LocalDate.parse("2023-03-22"),
                LocalDate.parse("2023-03-14"),
                LocalDate.parse("2023-03-14"),
                false);

        final BookingDTOResponse bookingDTOResponse = bookingService.parseEntityToDTOResponse(validBooking);
        when(repository.save(any(Booking.class))).thenReturn(validBooking);
        final BookingDTOResponse response = bookingService.save(bookingDTORequest);

        assertEquals(bookingDTOResponse.getStartDate(),response.getStartDate());
    }

    @Test
    public void saveADateWithMoreThan3Days() {
        final BookingDTORequest bookingDTORequest = new BookingDTORequest(
                "12345",
                "John Connor",
                "2023-03-20",
                "2023-03-24");

        final BookingDTOResponse response = bookingService.save(bookingDTORequest);
        assertEquals(null,response);

    }

    @Test
    public void testSaveStartDateBeforeEndDate() {

        final BookingDTORequest bookingDTORequest = new BookingDTORequest(
                "12345",
                "John Connor",
                "2023-03-24",
                "2023-03-20");

        final BookingDTOResponse response = bookingService.save(bookingDTORequest);
        assertEquals(null,response);
    }

    @Test
    public void test30DaysAdvanced() {

        final BookingDTORequest bookingDTORequest = new BookingDTORequest(
                "12345",
                "John Connor",
                "2023-05-01",
                "2023-05-03");

        final BookingDTOResponse response = bookingService.save(bookingDTORequest);
        assertEquals(null,response);
    }

    @Test
    public void testBookingToday() {
        final BookingDTORequest bookingDTORequest = new BookingDTORequest(
                "12345",
                "John Connor",
                "2023-03-13",
                "2023-03-15");

        final BookingDTOResponse response = bookingService.save(bookingDTORequest);
        assertEquals(null,response);

    }

    @Test
    public void testBookingYesterday() {
        final BookingDTORequest bookingDTORequest = new BookingDTORequest(
                "12345",
                "John Connor",
                "2023-03-12",
                "2023-03-14");

        final BookingDTOResponse response = bookingService.save(bookingDTORequest);
        assertEquals(null,response);

    }

    @Test
    public void testSaveFinishDateBeforeStartDate() {

        final BookingDTORequest bookingDTORequest = new BookingDTORequest(
                "12345",
                "John Connor",
                "2023-03-21",
                "2023-03-19");

        final BookingDTOResponse response = bookingService.save(bookingDTORequest);
        assertEquals(null,response);
    }

    @Test
    public void testSaveStartDateConflictToAnotherStartDate() {
        final BookingDTORequest bookingDTORequest = new BookingDTORequest(
                "12345",
                "John Connor",
                "2023-03-20",
                "2023-03-22");

        final Booking validBooking = new Booking(
                1L,
                "12345",
                "John Connor",
                LocalDate.parse("2023-03-20"),
                LocalDate.parse("2023-03-22"),
                LocalDate.parse("2023-03-14"),
                LocalDate.parse("2023-03-14"),
                false);

        final List<Booking> alreadyBooked = new ArrayList<>();
        alreadyBooked.add(validBooking);

        when(repository.findAll()).thenReturn(alreadyBooked);
        assertEquals(false,bookingService.availabilityDays(bookingDTORequest));

    }

    @Test
    public void testSaveFinishDateConflictWithAnotherFinishDate() {
        final BookingDTORequest bookingDTORequest = new BookingDTORequest(
                "12345",
                "John Connor",
                "2023-03-20",
                "2023-03-22");

        final Booking validBooking = new Booking(
                1L,
                "12345",
                "John Connor",
                LocalDate.parse("2023-03-20"),
                LocalDate.parse("2023-03-22"),
                LocalDate.parse("2023-03-14"),
                LocalDate.parse("2023-03-14"),
                false);

        final List<Booking> alreadyBooked = new ArrayList<>();
        alreadyBooked.add(validBooking);

        when(repository.findAll()).thenReturn(alreadyBooked);
        assertEquals(false,bookingService.availabilityDays(bookingDTORequest));

    }

    @Test
    public void testSaveStartDateisBetweenBookedDate() {
        final BookingDTORequest bookingDTORequest = new BookingDTORequest(
                "12345",
                "John Connor",
                "2023-03-20",
                "2023-03-22");

        final Booking validBooking = new Booking(
                1L,
                "12345",
                "John Connor",
                LocalDate.parse("2023-03-21"),
                LocalDate.parse("2023-03-23"),
                LocalDate.parse("2023-03-14"),
                LocalDate.parse("2023-03-14"),
                false);

        final List<Booking> alreadyBooked = new ArrayList<>();
        alreadyBooked.add(validBooking);

        when(repository.findAll()).thenReturn(alreadyBooked);
        assertEquals(false,bookingService.availabilityDays(bookingDTORequest));

    }

    @Test
    public void testSaveStartDateAfterFinishBookedDate() {
        final BookingDTORequest bookingDTORequest = new BookingDTORequest(
                "12345",
                "John Connor",
                "2023-03-20",
                "2023-03-22");

        final Booking validBooking = new Booking(
                1L,
                "12345",
                "John Connor",
                LocalDate.parse("2023-03-23"),
                LocalDate.parse("2023-03-20"),
                LocalDate.parse("2023-03-14"),
                LocalDate.parse("2023-03-14"),
                false);

        final List<Booking> alreadyBooked = new ArrayList<>();
        alreadyBooked.add(validBooking);

        when(repository.findAll()).thenReturn(alreadyBooked);
        assertEquals(false,bookingService.availabilityDays(bookingDTORequest));

    }

}