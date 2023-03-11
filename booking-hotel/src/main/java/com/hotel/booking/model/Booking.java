package com.hotel.booking.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "bok_booking")
@Setter
@Getter
@EqualsAndHashCode
public class Booking {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "bok_idCard")
    private String idCard;

    @Column(name = "bok_name", nullable = false)
    private String name;

    @Column(name = "bok_startDate", nullable = false)
    private LocalDate startDate;

    @Column(name = "bok_finishDate", nullable = false)
    private LocalDate finishDate;

    @Column(name = "bok_createdDate")
    private LocalDate createdDate;

    @Column(name = "bok_updatedDate")
    private LocalDate updatedDate;

    @Column(name = "bok_expired")
    private boolean expired;

    public Booking () {}

    public Booking(Long id, String idCard, String name, LocalDate startDate, LocalDate finishDate, LocalDate createdDate, LocalDate updatedDate, boolean expired) {
        this.id = id;
        this.idCard = idCard;
        this.name = name;
        this.startDate = startDate;
        this.finishDate = finishDate;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
        this.expired = expired;
    }


}
