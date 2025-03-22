package com.att.tdp.popcorn_palace.Repositories;
import com.att.tdp.popcorn_palace.Entities.Booking;
import com.att.tdp.popcorn_palace.Entities.Showtime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.UUID;

public interface BookingRepository extends JpaRepository<Booking, UUID> {




    boolean existsByShowtimeIdAndSeatNumber(long showtimeId, int seatNumber);
}


