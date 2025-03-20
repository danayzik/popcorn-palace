package com.att.tdp.popcorn_palace.Services;

import com.att.tdp.popcorn_palace.Entities.Movie;
import com.att.tdp.popcorn_palace.Repositories.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MovieService{

    @Autowired
    private MovieRepository movieRepository;

    public boolean existsByTitle(String title){
        return movieRepository.existsByTitle(title);
    }

    public Movie create(Movie movie){

        return movieRepository.saveAndFlush(movie);
    }

    public Movie getOne(String title){
        Optional<Movie> movieOption = movieRepository.findByTitle(title);
        return movieOption.orElse(null);
    }

    public List<Movie> getAll(){
        return movieRepository.findAll();
    }

    public boolean update(Movie oldMovie, Movie newMovie){
        oldMovie.patch(newMovie);
        if(!newMovie.isValid())
            return false;
        movieRepository.saveAndFlush(oldMovie);
        return true;
    }

    public boolean delete(long id){
        movieRepository.deleteById(id);
        return !movieRepository.existsById(id);
    }





}
