package com.att.tdp.popcorn_palace.Services;

import com.att.tdp.popcorn_palace.Dtos.BookingDto;
import com.att.tdp.popcorn_palace.Entities.Booking;
import com.att.tdp.popcorn_palace.Entities.Showtime;
import com.att.tdp.popcorn_palace.Exceptions.IncorrectFieldException;
import com.att.tdp.popcorn_palace.Repositories.BookingRepository;
import com.att.tdp.popcorn_palace.Repositories.ShowtimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private ShowtimeRepository showtimeRepository;


    public Booking create(BookingDto bookingDto) {
        Showtime showtime = showtimeRepository.findById(bookingDto.getShowtimeId())
                .orElseThrow(() -> new IncorrectFieldException("showtimeId not found."));
        if(bookingRepository.existsByShowtimeIdAndSeatNumber(bookingDto.getShowtimeId(), bookingDto.getSeatNumber()))
            throw new IncorrectFieldException("Seat already taken.");

        Booking newBooking = new Booking(showtime, bookingDto);
        if (!newBooking.isValid()) {
            throw new IncorrectFieldException("Seat number can't be negative");
        }
        showtime.getBookings().add(newBooking);

        return bookingRepository.saveAndFlush(newBooking);
    }



}
