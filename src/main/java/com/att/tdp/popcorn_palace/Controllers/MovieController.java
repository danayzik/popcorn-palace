package com.att.tdp.popcorn_palace.Controllers;
import com.att.tdp.popcorn_palace.Entities.Movie;
import com.att.tdp.popcorn_palace.Services.MovieService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/movies")
public class MovieController {

    @Autowired
    MovieService movieService;

    @PostMapping
    public ResponseEntity<Movie> create(@RequestBody Movie movie) {
        return new ResponseEntity<>(movieService.create(movie), HttpStatus.CREATED);
    }

    @GetMapping("/{title}")
    public ResponseEntity<Movie> get(@PathVariable String title) {
        return new ResponseEntity<>(movieService.getOne(title), HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Movie>> getAll(){
        return new ResponseEntity<>(movieService.getAll(), HttpStatus.OK);
    }

    @PostMapping("/update/{title}")
    public ResponseEntity<Movie> update(@PathVariable String title, @RequestBody Movie newMovie) {
        movieService.update(title , newMovie);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{title}")
    public ResponseEntity<Movie> delete(@PathVariable String title) {
        return movieService.delete(title) ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

    }

//    @PostConstruct
//    protected void initSomeMovies() {
//        movieService.create(new Movie("Titanic", "Action", 1997, 8.9, 120));
//        movieService.create(new Movie("Ip Man", "s", 1997, 8.9, 22));
//        movieService.create(new Movie("Ligma", "s", 1997, 8.9, 2));
//        movieService.create(new Movie("Sugma", "d", 1997, 8.9, 23));
//        movieService.create(new Movie("Socon", "d", 1997, 8.9, 54));
//        movieService.create(new Movie("Chocon", "d", 1997, 8.9, 54));
//    }


}
