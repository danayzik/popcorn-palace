package com.att.tdp.popcorn_palace.Services;

import com.att.tdp.popcorn_palace.Dtos.ShowtimeDto;
import com.att.tdp.popcorn_palace.Entities.Movie;
import com.att.tdp.popcorn_palace.Entities.Showtime;
import com.att.tdp.popcorn_palace.Exceptions.DatabaseInconsistencyException;
import com.att.tdp.popcorn_palace.Exceptions.IncorrectFieldException;
import com.att.tdp.popcorn_palace.Exceptions.ResourceNotFoundException;
import com.att.tdp.popcorn_palace.Repositories.MovieRepository;
import com.att.tdp.popcorn_palace.Repositories.ShowtimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class ShowtimesService {

    @Autowired
    private ShowtimeRepository showtimeRepository;

    @Autowired
    private MovieRepository movieRepository;


    public Showtime create(ShowtimeDto showtimeDto) {
        Movie movie = movieRepository.findById(showtimeDto.getMovieId())
                .orElseThrow(() -> new IncorrectFieldException("movieId not found."));
        boolean overlapExists = showtimeRepository.existsOverlappingShowtime(
                showtimeDto.getTheater(), showtimeDto.getStartTime(), showtimeDto.getEndTime(), -1L
        );
        if(overlapExists)
            throw new IncorrectFieldException("The show has overlapping times with an existing showtime");
        Showtime newShowtime = new Showtime(movie, showtimeDto);
        if (!newShowtime.isValid()) {
            throw new IncorrectFieldException("One or more field values are incorrect.");
        }
        movie.getShowtimes().add(newShowtime);
        return showtimeRepository.saveAndFlush(newShowtime);
    }

    public Showtime getOne(long id){
        Showtime showtime = showtimeRepository.findById(id).orElse(null);
        if (showtime == null) {
            throw new ResourceNotFoundException(String.format("Showtime with id %d not found", id));
        }
        return showtime;
    }



    public void update(ShowtimeDto showtimeDto, long showtimeId){
        Showtime oldShowtime = showtimeRepository.findById(showtimeId).
                orElseThrow(() -> new ResourceNotFoundException(String.format("Showtime with id %d not found", showtimeId)));

        long newMovieId = showtimeDto.getMovieId();
        Movie newMovie = movieRepository.findById(newMovieId).
                orElseThrow(() -> new ResourceNotFoundException(String.format("Movie with id %d not found", newMovieId)));
        long oldMovieId = oldShowtime.getMovieId();
        boolean movieWasUpdated = newMovie.getId() != oldMovieId;
        if(!oldShowtime.patch(showtimeDto))
            throw new IncorrectFieldException("One or more field values are incorrect.");
        oldShowtime.setMovie(newMovie);
        boolean overlapExists = showtimeRepository.existsOverlappingShowtime(
                showtimeDto.getTheater(), showtimeDto.getStartTime(), showtimeDto.getEndTime(), showtimeId
        );
        if(overlapExists)
            throw new IncorrectFieldException("The updated show has overlapping times with an existing showtime");
        if(movieWasUpdated){
            Movie oldMovie = movieRepository.findById(oldMovieId).
                    orElseThrow(() -> new DatabaseInconsistencyException("Database is inconsistent, internal error"));
            oldMovie.getShowtimes().remove(oldShowtime);
            newMovie.getShowtimes().add(oldShowtime);
        }
        showtimeRepository.saveAndFlush(oldShowtime);
    }


    public boolean delete(long showtimeId){
        if(!showtimeRepository.existsById(showtimeId))
            throw new ResourceNotFoundException(String.format("Showtime with id %d not found", showtimeId));
        showtimeRepository.deleteById(showtimeId);
        return !showtimeRepository.existsById(showtimeId);

    }





}
