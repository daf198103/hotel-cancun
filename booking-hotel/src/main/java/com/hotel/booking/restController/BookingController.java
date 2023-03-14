package com.hotel.booking.restController;

import com.hotel.booking.dto.BookingDTORequest;
import com.hotel.booking.dto.BookingDTOResponse;
import com.hotel.booking.service.impl.BookingServiceImpl;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/booking")
public class BookingController {

    private static final Logger LOGGER = LoggerFactory.getLogger(BookingController.class);


    private final BookingServiceImpl bookingService;

    @Autowired
    public BookingController(BookingServiceImpl bookingService) {
        this.bookingService = bookingService;
    }


    @ApiOperation(value = "Booking a new reservation")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Reservation successfully done"),
            @ApiResponse(code = 404, message = "Something went wrong, review data and do it again")
    })
    @ResponseStatus(code = HttpStatus.CREATED)
    @PostMapping(value = "/create")
    public ResponseEntity<BookingDTOResponse> createReservation(
            @RequestBody @ApiParam(value = "The booking data")
            @Valid final BookingDTORequest bookingDTORequest) {
        LOGGER.info("Booking Request: {}.", bookingDTORequest);
        final BookingDTOResponse bookingResponseDTO = bookingService.save(bookingDTORequest);
        if(Optional.ofNullable(bookingResponseDTO).isPresent()) {
            return new ResponseEntity(bookingResponseDTO, HttpStatus.CREATED);
        } else {
            return new ResponseEntity(bookingResponseDTO, HttpStatus.NOT_FOUND);
        }
    }


    @ApiOperation(value = "Find all booking list")
    @GetMapping(value = "/findAll")
    public ResponseEntity<List<BookingDTOResponse>> findAll() {
        LOGGER.info("Finding All bookings ");
        final List<BookingDTOResponse> responseDTOlist;
        responseDTOlist = bookingService.findAll();
        if(!responseDTOlist.isEmpty()) {
            return new ResponseEntity(responseDTOlist,HttpStatus.OK);
        } else {
            return new ResponseEntity(responseDTOlist,HttpStatus.NO_CONTENT);
        }
    }


    @ApiOperation(value = "Find a booking by its id")
    @GetMapping(value = "/{id}")
    public ResponseEntity<BookingDTOResponse> findById(@PathVariable("id") @ApiParam(value = "The booking id")
                                                                 final Long id) {
        LOGGER.info("Finding booking by id: {}.", id);
        final BookingDTOResponse responseDTO;
        responseDTO = bookingService.findById(id);
        if(Optional.ofNullable(responseDTO.getId()).isPresent()) {
            return new ResponseEntity(responseDTO,HttpStatus.OK);
        } else {
            return new ResponseEntity(responseDTO,HttpStatus.NO_CONTENT);
        }
    }

    @ApiOperation(value = "Delete a booking by its id")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable("id") @ApiParam(value = "The booking id")
                                           final Long id) throws Exception {
        LOGGER.info("Deleting booking by id: {}.", id);
        bookingService.deleteById(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ApiOperation(value = "Update a booking by its id")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "No Content"),
            @ApiResponse(code = 404, message = "Booking not found"),
            @ApiResponse(code = 400, message = "Ops...something went wrong with your request")
    })
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    @PutMapping(value = "/update/{id}")
    public ResponseEntity<Void> update(@RequestBody @ApiParam(value = "The booking data")
                                       @Valid final BookingDTORequest bookingDTORequest,
                                       @PathVariable("id") @ApiParam(value = "The booking id")
                                       final Long id) throws Exception {
        LOGGER.info("Updating booking with id {} and body request {}.", id, bookingDTORequest);
        bookingService.update(id,bookingDTORequest);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

}
