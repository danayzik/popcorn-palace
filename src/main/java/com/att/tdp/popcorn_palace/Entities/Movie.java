package com.att.tdp.popcorn_palace.Entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name="movies")
public class Movie {

    @Column(unique = true)
    @Getter
    @Setter
    private String title = "";

    @Getter
    @Setter
    private String genre = "";

    @Getter
    @Setter
    private int duration = -1;

    @Getter
    @Setter
    private double rating = -1;

    @Getter
    @Setter
    private int releaseYear = -1;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Getter
    private long id;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.REMOVE)
    @JsonIgnore
    private List<Showtime> showtimes = new ArrayList<>();



    public boolean patch(Movie movie){
        this.genre = movie.getGenre();
        this.duration = movie.getDuration();
        this.rating = movie.getRating();
        this.releaseYear = movie.getReleaseYear();
        this.title = movie.getTitle();
        return isValid();
    }

    @JsonIgnore
    public List<Showtime> getShowtimes(){
        return showtimes;
    }


    //For testing purposes
    @JsonIgnore
    public Movie(String title, String genre, int duration, double rating, int releaseYear) {
        this.title = title;
        this.genre = genre;
        this.duration = duration;
        this.rating = rating;
        this.releaseYear = releaseYear;
    }

    @JsonIgnore
    public boolean isValid(){
        boolean validRating =  rating >= 0 && rating <= 10;
        boolean validDuration =  duration > 0;
        boolean releaseYearValid = releaseYear > 0;
        boolean genreValid = genre != null && !genre.isBlank();
        boolean titleValid = title!= null && !title.isBlank();
        return validDuration && validRating && releaseYearValid && genreValid && titleValid;
    }

}
