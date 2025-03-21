package com.att.tdp.popcorn_palace.Dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ShowtimeDto {
    private long id = -1;
    private double price = -1;
    private String theater = "";
    private Instant startTime = null;
    private Instant endTime = null;
    private long movieId = -1;


}