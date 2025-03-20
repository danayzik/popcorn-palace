package com.att.tdp.popcorn_palace.Repositories;
import com.att.tdp.popcorn_palace.Entities.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MovieRepository extends JpaRepository<Movie, Long> {

    boolean existsByTitle(String title);


    Optional<Movie> findByTitle(String title);
}
