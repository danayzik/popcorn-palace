package com.att.tdp.popcorn_palace.Controllers;
import com.att.tdp.popcorn_palace.Entities.Movie;
import com.att.tdp.popcorn_palace.Services.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/movies")
public class MovieController {

    @Autowired
    MovieService movieService;


    @PostMapping
    public ResponseEntity<Movie> create(@RequestBody Movie newMovie) {
        if(movieService.existsByTitle(newMovie.getTitle())) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        if(!newMovie.isValid())
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        Movie movie = movieService.create(newMovie);
        return new ResponseEntity<>(movie, HttpStatus.OK);
    }

    @GetMapping("/{title}")
    public ResponseEntity<Movie> getOne(@PathVariable String title) {
        Movie movie = movieService.getOne(title);
        if(movie!=null){
            return new ResponseEntity<>(movie, HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<Movie>> getAll() {
        List<Movie> movies = movieService.getAll();
        return new ResponseEntity<>(movies, HttpStatus.OK);
    }

    @PostMapping("/update/{title}")
    public ResponseEntity<Movie> update(@PathVariable String title, @RequestBody Movie newMovie) {
        Movie oldMovie = movieService.getOne(title);
        if(oldMovie==null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        boolean updated = movieService.update(oldMovie, newMovie);
        if(!updated)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{title}")
    public ResponseEntity<Movie> delete(@PathVariable String title) {
        Movie movieOption = movieService.getOne(title);
        if(movieOption==null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        boolean deleted = movieService.delete(movieOption.getId());
        if(!deleted){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.OK);

    }


}
