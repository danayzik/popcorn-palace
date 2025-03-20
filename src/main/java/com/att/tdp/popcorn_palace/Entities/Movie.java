package com.att.tdp.popcorn_palace.Entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private long id;



    public boolean patch(Movie movie){
        this.genre = !movie.getGenre().isBlank() ? movie.getGenre() : this.genre;
        this.duration = (movie.getDuration() > 0) ? movie.getDuration() : this.duration;
        this.rating = (movie.getRating() > 0) ? movie.getRating() : this.rating;
        this.releaseYear = (movie.getReleaseYear() > 0) ? movie.getReleaseYear() : this.releaseYear;
        return isValid();
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
