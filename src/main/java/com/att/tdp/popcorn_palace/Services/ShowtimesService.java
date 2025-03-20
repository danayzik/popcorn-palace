package com.att.tdp.popcorn_palace.Services;

import com.att.tdp.popcorn_palace.Entities.Movie;
import com.att.tdp.popcorn_palace.Entities.Showtime;
import com.att.tdp.popcorn_palace.Exceptions.IncorrectFieldException;
import com.att.tdp.popcorn_palace.Exceptions.ResourceAlreadyExistsException;
import com.att.tdp.popcorn_palace.Exceptions.ResourceNotFoundException;
import com.att.tdp.popcorn_palace.Repositories.MovieRepository;
import com.att.tdp.popcorn_palace.Repositories.ShowtimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ShowtimesService {

    @Autowired
    private ShowtimeRepository showtimeRepository;

    @Autowired
    private MovieRepository movieRepository;


    public Showtime create(Showtime showtime) {
        if (!showtime.isValid()) {
            throw new IncorrectFieldException("One or more field values are incorrect.");
        }

        return showtimeRepository.saveAndFlush(showtime);
    }

    public Showtime getOne(long id){
        Showtime showtime = showtimeRepository.findById(id).orElse(null);
        if (showtime == null) {
            throw new ResourceNotFoundException(String.format("Showtime with id %d not found", id));
        }
        return showtime;
    }




    public void update(String oldTitle, Movie newMovie){


    }

    public boolean delete(String title){
        return true;

    }





}
