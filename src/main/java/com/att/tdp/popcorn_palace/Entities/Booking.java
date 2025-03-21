package com.att.tdp.popcorn_palace.Entities;


import com.att.tdp.popcorn_palace.Dtos.BookingDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="bookings")
public class Booking {

    @ManyToOne
    @JoinColumn(name = "showtimeId", nullable = false)
    @JsonIgnore
    private Showtime showtime;


    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private int seatNumber = -1;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private UUID userId = null;


    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID bookingId;

    @JsonIgnore
    public Booking(Showtime showtime, BookingDto bookingDto) {
        this.showtime = showtime;
        this.seatNumber = bookingDto.getSeatNumber();
        this.userId = bookingDto.getUserId();
    }

    @JsonIgnore
    public boolean isValid(){
        return seatNumber>=0;
    }





}
