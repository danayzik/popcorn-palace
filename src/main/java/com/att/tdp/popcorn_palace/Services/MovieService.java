package com.att.tdp.popcorn_palace.Services;

import com.att.tdp.popcorn_palace.Entities.Movie;
import com.att.tdp.popcorn_palace.Exceptions.IncorrectFieldException;
import com.att.tdp.popcorn_palace.Exceptions.ResourceNotFoundException;
import com.att.tdp.popcorn_palace.Exceptions.ResourceAlreadyExistsException;
import com.att.tdp.popcorn_palace.Repositories.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
public class MovieService{

    @Autowired
    private MovieRepository movieRepository;


    public Movie create(Movie movie) {
        if (!movie.isValid()) {
            throw new IncorrectFieldException("One or more fields are incorrect.");
        }

        if (movieRepository.existsByTitle(movie.getTitle())){
            throw new ResourceAlreadyExistsException(String.format("Movie with title %s not found", movie.getTitle()));
        }

        return movieRepository.saveAndFlush(movie);
    }

    public Movie getOne(String title){
        Movie movie = movieRepository.findByTitle(title).orElse(null);
        if (movie == null) {
            throw new ResourceNotFoundException(String.format("Movie with title %s not found", title));
        }
        return movie;
    }


    public List<Movie> getAll(){
        return movieRepository.findAll();
    }

    public void update(String oldTitle, Movie newMovie){
        Movie oldMovie = movieRepository.findByTitle(oldTitle).orElse(null);
        if (oldMovie == null) {
            throw new ResourceNotFoundException(String.format("Movie with title %s not found",oldTitle));
        }
        if (!oldMovie.patch(newMovie)) {
            throw new IncorrectFieldException("One or more fields are incorrect.");
        }
        movieRepository.saveAndFlush(oldMovie);
    }


    public boolean delete(String title){
        if (!movieRepository.existsByTitle(title)){
            throw new ResourceNotFoundException(String.format("Movie with title %s not found", title));
        }
        movieRepository.deleteByTitle(title);
        return !movieRepository.existsByTitle(title);

    }





}
