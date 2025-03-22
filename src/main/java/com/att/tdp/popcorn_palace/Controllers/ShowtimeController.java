package com.att.tdp.popcorn_palace.Controllers;

import com.att.tdp.popcorn_palace.Dtos.ShowtimeDto;
import com.att.tdp.popcorn_palace.Entities.Showtime;
import com.att.tdp.popcorn_palace.Services.ShowtimesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/showtimes")
public class ShowtimeController {

    @Autowired
    ShowtimesService showtimesService;

    @PostMapping
    public ResponseEntity<Showtime> create(@RequestBody ShowtimeDto showtimeDto) {
        return new ResponseEntity<>(showtimesService.create(showtimeDto), HttpStatus.CREATED);
    }

    @GetMapping("/{showtimeId}")
    public ResponseEntity<Showtime> get(@PathVariable long showtimeId) {
        return new ResponseEntity<>(showtimesService.getOne(showtimeId), HttpStatus.OK);
    }


    @PostMapping("/update/{showtimeId}")
    public ResponseEntity<Void> update(@PathVariable long showtimeId, @RequestBody ShowtimeDto showtimeDto) {
        showtimesService.update(showtimeDto , showtimeId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{showtimeId}")
    public ResponseEntity<Void> delete(@PathVariable long showtimeId) {
        return showtimesService.delete(showtimeId) ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

    }




}
