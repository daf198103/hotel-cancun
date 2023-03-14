package com.hotel.booking.controller;

import com.hotel.booking.dto.BookingDTORequest;
import com.hotel.booking.dto.BookingDTOResponse;
import com.hotel.booking.restController.BookingController;
import com.hotel.booking.service.impl.BookingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookingController.class)
public class BookingControllerTest {

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private BookingServiceImpl bookingServiceimpl;

    private static final LocalDate NOW = LocalDate.now();

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("uuuu-MM-dd");

    protected MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(this.context)
                .build();
    }

    @Test
    public void testCreateBookingValidData() throws Exception {
        when(bookingServiceimpl.save(any())).thenReturn(
                new BookingDTOResponse(
                1L,
                "12345",
                "John Connor",
                "2023-03-20",
                "2023-03-22"
        ));

        this.mockMvc
                .perform(post("/api/v1/booking/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                "{\"idCard\": \"" + "12345" + "\"," +
                                " \"name\": \"" + "John Connor" + "\"," +
                                " \"startDate\": \"" + "2023-03-20" + "\"," +
                                " \"finishDate\": \"" + "2023-03-22" +
                                " \"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.idCard").value("12345"))
                .andExpect(jsonPath("$.name").value("John Connor"))
                .andExpect(jsonPath("$.startDate").value("2023-03-20"))
                .andExpect(jsonPath("$.finishDate").value("2023-03-22"));
    }


    @Test
    public void testCreateBookingInvalidData() throws Exception {
        when(bookingServiceimpl.save(any())).thenReturn(
                null);

        this.mockMvc
                .perform(post("/api/v1/booking/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                "{\"idCard\": \"" + null + "\"," +
                                        " \"name\": \"" + "" + "\"," +
                                        " \"startDate\": \"" + "" + "\"," +
                                        " \"finishDate\": \"" + "" +
                                        " \"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testFindBookingById() throws Exception {
        when(bookingServiceimpl.findById(1L)).thenReturn(
                new BookingDTOResponse(
                        1L,
                        "12345",
                        "John Connor",
                        "2023-03-08",
                        "2023-03-10"
                ));

        this.mockMvc
                .perform(get("/api/v1/booking/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.idCard").value("12345"))
                .andExpect(jsonPath("$.name").value("John Connor"))
                .andExpect(jsonPath("$.startDate").value("2023-03-08"))
                .andExpect(jsonPath("$.finishDate").value("2023-03-10"));
    }

    @Test
    public void testNotFindBookingById() throws Exception {
        when(bookingServiceimpl.findById(0L)).thenReturn(new BookingDTOResponse());

        this.mockMvc
                .perform(get("/api/v1/booking/0"))
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$.id").isEmpty())
                .andExpect(jsonPath("$.idCard").isEmpty())
                .andExpect(jsonPath("$.name").isEmpty())
                .andExpect(jsonPath("$.startDate").isEmpty())
                .andExpect(jsonPath("$.finishDate").isEmpty());
    }

    @Test
    public void testFindAllBookings() throws Exception {
        when(bookingServiceimpl.findAll()).thenReturn(
                Collections.singletonList(
                        new BookingDTOResponse(
                        1L,
                        "12345",
                        "John Connor",
                        "2023-03-08",
                        "2023-03-10"
                )));

        this.mockMvc
                .perform(get("/api/v1/booking/findAll"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].idCard").value("12345"))
                .andExpect(jsonPath("$[0].name").value("John Connor"))
                .andExpect(jsonPath("$[0].startDate").value("2023-03-08"))
                .andExpect(jsonPath("$[0].finishDate").value("2023-03-10"));
    }



    @Test
    public void testDeleteBookingById() throws Exception {

        doNothing().when(bookingServiceimpl).deleteById(1L);
        this.mockMvc
                .perform(delete("/api/v1/booking/delete/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testUpdateBookingValidData() throws Exception {
        doNothing().when(bookingServiceimpl).update(1L ,new BookingDTORequest(
                "12345",
                "John Connor",
                "2023-03-20",
                "2023-03-22"
        ));

        this.mockMvc
                .perform(put("/api/v1/booking/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                "{\"idCard\": \"" + "12345" + "\"," +
                                        " \"name\": \"" + "John Connor" + "\"," +
                                        " \"startDate\": \"" + "2023-03-20" + "\"," +
                                        " \"finishDate\": \"" + "2023-03-22" +
                                        " \"}"))
                .andExpect(status().isNoContent());
    }

}
