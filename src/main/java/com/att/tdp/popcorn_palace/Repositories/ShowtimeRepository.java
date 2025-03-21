package com.att.tdp.popcorn_palace.Repositories;
import com.att.tdp.popcorn_palace.Entities.Movie;
import com.att.tdp.popcorn_palace.Entities.Showtime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.Optional;

public interface ShowtimeRepository extends JpaRepository<Showtime, Long> {

    @Query("""
        SELECT COUNT(s) > 0 FROM Showtime s
        WHERE s.theater = :theater
        AND s.startTime < :endTime
        AND s.endTime > :startTime
        AND s.id <> : id
    """)
    boolean existsOverlappingShowtime(@Param("theater") String theater,
                                      @Param("startTime") Instant startTime,
                                      @Param("endTime") Instant endTime,
                                      @Param("id") long id);
}


