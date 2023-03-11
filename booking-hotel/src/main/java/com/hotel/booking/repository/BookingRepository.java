package com.hotel.booking.repository;

import com.hotel.booking.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {



    @Modifying //used when is update
    @Query("UPDATE Booking b SET b.expired = true WHERE id = :id")
    void deleteLogicallyById(@Param("id")Long id);


}
