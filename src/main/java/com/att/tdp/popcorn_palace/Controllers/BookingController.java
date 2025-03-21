package com.att.tdp.popcorn_palace.Controllers;

import com.att.tdp.popcorn_palace.Dtos.BookingDto;
import com.att.tdp.popcorn_palace.Dtos.ShowtimeDto;
import com.att.tdp.popcorn_palace.Entities.Booking;
import com.att.tdp.popcorn_palace.Entities.Showtime;
import com.att.tdp.popcorn_palace.Services.BookingService;
import com.att.tdp.popcorn_palace.Services.ShowtimesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/bookings")
public class BookingController {

    @Autowired
    BookingService bookingService;

    @PostMapping
    public ResponseEntity<Booking> create(@RequestBody BookingDto bookingDto) {
        return new ResponseEntity<>(bookingService.create(bookingDto), HttpStatus.CREATED);
    }




}
