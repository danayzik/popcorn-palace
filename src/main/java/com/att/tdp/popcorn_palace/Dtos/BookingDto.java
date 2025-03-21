package com.att.tdp.popcorn_palace.Dtos;

import com.att.tdp.popcorn_palace.Entities.Showtime;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingDto {

    private long showtimeId;

    private int seatNumber = -1;

    private UUID userId = null;





}