package com.att.tdp.popcorn_palace.Entities;


import com.att.tdp.popcorn_palace.Dtos.ShowtimeDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name="showtimes")
public class Showtime {


    @ManyToOne
    @JoinColumn(name = "movieId", nullable = false)
    @JsonIgnore
    private Movie movie;

    @Getter
    @Setter
    private String theater;

    @Getter
    @Setter
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", timezone = "UTC")
    private Instant startTime;
    @Getter
    @Setter
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", timezone = "UTC")
    private Instant endTime;

    @Getter
    @Setter
    private double price;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Getter
    private long id;

    @OneToMany(mappedBy = "showtime", cascade = CascadeType.REMOVE)
    @JsonIgnore
    private List<Booking> bookings = new ArrayList<>();




    public long getMovieId(){
        return movie.getId();
    }

    @JsonIgnore
    public List<Booking> getBookings(){
        return bookings;
    }

    @JsonIgnore
    public void setMovie(Movie movie){
        this.movie = movie;
    }

    @JsonIgnore
    public Showtime(Movie movie, ShowtimeDto showtimeDto) {
        this.movie = movie;
        this.endTime = showtimeDto.getEndTime();
        this.startTime = showtimeDto.getStartTime();
        this.price = showtimeDto.getPrice();
        this.theater = showtimeDto.getTheater();
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

    public boolean patch(ShowtimeDto showtimeDto){
        this.theater = showtimeDto.getTheater();
        this.startTime = showtimeDto.getStartTime();
        this.endTime = showtimeDto.getEndTime();
        this.price = showtimeDto.getPrice();
        return isValid();
    }










}
