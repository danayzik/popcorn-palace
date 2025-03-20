package com.att.tdp.popcorn_palace.Repositories;
import com.att.tdp.popcorn_palace.Entities.Movie;
import com.att.tdp.popcorn_palace.Entities.Showtime;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShowtimeRepository extends JpaRepository<Showtime, Long> {


}
