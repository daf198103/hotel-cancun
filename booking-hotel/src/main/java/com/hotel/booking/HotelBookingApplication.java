package com.hotel.booking;

import com.hotel.booking.config.Swagger2Configuration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@SpringBootApplication
@Import(Swagger2Configuration.class)
@EnableAutoConfiguration
@EnableSwagger2
public class HotelBookingApplication {


	public static void main(String[] args) { SpringApplication.run(HotelBookingApplication.class, args);

	}
}
