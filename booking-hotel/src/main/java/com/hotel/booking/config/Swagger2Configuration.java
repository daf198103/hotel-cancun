package com.hotel.booking.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;

@EnableSwagger2
@Configuration
public class Swagger2Configuration {

    @Bean
    public Docket productApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.hotel.booking"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(metaInfo())
                .useDefaultResponseMessages(false);
    }

    private ApiInfo metaInfo() {
        ApiInfo apiInfo = new ApiInfo(
                "Booking Hotel Cancun API REST",
                "Room Reservation API",
                "1.0",
                "Terms of Service",
                new Contact("David Alves França", "https://www.alten.ca/fr/", "dafranca1981@gmail.com"),
                "",
                "",
                new ArrayList<>());
        return apiInfo;
        }

}
