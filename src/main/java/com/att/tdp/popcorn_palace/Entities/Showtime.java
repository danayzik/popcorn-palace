package com.att.tdp.popcorn_palace.Entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Duration;
import java.time.Instant;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="showtimes")
public class Showtime {


    @ManyToOne
    @JoinColumn(name = "movieId", nullable = false)
    @JsonIgnore
    private Movie movie;

    private String theater;


    private Instant startTime;
    private Instant endTime;

    private double price;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private long id;


    public long getMovieId(){
        return movie.getId();
    }

    @JsonIgnore
    public boolean isValid() {
        if(startTime == null)
            return false;
        if(endTime == null)
            return false;
        boolean validTheater = theater != null && !theater.isBlank();
        boolean validEndTime = endTime != null && !endTime.isBefore(startTime);
        boolean validPrice = price >= 0;
        boolean validDuration = Duration.between(startTime, endTime).toMinutes() >= movie.getDuration();
        return validTheater && validEndTime && validPrice && validDuration;
    }






}
