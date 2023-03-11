package com.hotel.booking.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Setter
@Getter
public class BookingDTORequest {


    @NotNull(message = "Personal Identification (idCard).")
    @ApiModelProperty(example = "12345")
    private String idCard;

    @NotNull(message = "Name of the guest")
    @ApiModelProperty(example = "John Connor")
    private String name;

    @NotNull(message = "start date couldn't be blank")
    @ApiModelProperty(example = "2023-03-26")
    private String startDate;

    @NotNull(message = "finish date couldn't be blank")
    @ApiModelProperty(example = "2023-03-28")
    private String finishDate;

    public BookingDTORequest(String idCard, String name, String startDate, String finishDate) {
        this.idCard = idCard;
        this.name = name;
        this.startDate = startDate;
        this.finishDate = finishDate;
    }

    @Override
    public String toString() {
        return "BookingDTORequest {" +
                "  idCard='" + idCard + '\'' +
                ", name='" + name + '\'' +
                ", startDate=" + startDate +
                ", finishDate=" + finishDate +
                '}';
    }
}
