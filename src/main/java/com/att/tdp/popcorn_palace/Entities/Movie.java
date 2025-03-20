package com.att.tdp.popcorn_palace.Entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="movies")
public class Movie {

    @Column(unique = true)
    private String title = "";

    private String genre = "";

    private int duration = -1;

    private double rating = -1;

    private int releaseYear = -1;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private long id;



    public void patch(Movie movie){
        this.genre = !movie.getGenre().isBlank() ? movie.getGenre() : this.genre;
        this.duration = (movie.getDuration() > 0) ? movie.getDuration() : this.duration;
        this.rating = (movie.getRating() > 0) ? movie.getRating() : this.rating;
        this.releaseYear = (movie.getReleaseYear() > 0) ? movie.getReleaseYear() : this.releaseYear;
    }


    @JsonIgnore
    public boolean isValid(){
        boolean validRating =  rating >= 0 && rating <= 10;
        boolean validDuration =  duration > 0;
        boolean releaseYearValid = releaseYear > 0;
        boolean genreValid = !genre.isBlank();
        boolean titleValid = !title.isBlank();
        return validDuration && validRating && releaseYearValid && genreValid && titleValid;

    }







}
