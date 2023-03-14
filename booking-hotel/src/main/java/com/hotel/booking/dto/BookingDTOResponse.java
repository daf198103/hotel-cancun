package com.hotel.booking.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class BookingDTOResponse {

    @ApiModelProperty(example = "1")
    private Long id;

    @ApiModelProperty(example = "12345")
    private String idCard;

    @ApiModelProperty(example = "John Connor")
    private String name;

    @ApiModelProperty(example = "2023-04-26T00:00:00.000Z")
    private String startDate;

    @ApiModelProperty(example = "2023-04-29T23:59:59.999Z")
    private String finishDate;

    @ApiModelProperty(example = "true")
    private boolean expired;

    public BookingDTOResponse(Long id, String idCard, String name, String startDate, String finishDate) {
        this.id = id;
        this.idCard = idCard;
        this.name = name;
        this.startDate = startDate;
        this.finishDate = finishDate;
    }
    public BookingDTOResponse() {}
}
