package com.att.tdp.popcorn_palace.Repositories;
import com.att.tdp.popcorn_palace.Entities.Booking;
import com.att.tdp.popcorn_palace.Entities.Showtime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.UUID;

public interface BookingRepository extends JpaRepository<Booking, UUID> {


//    @Query("""
//        SELECT COUNT(b) > 0 FROM Booking b
//        WHERE b.showtime.id = :showtimeId
//        AND b.seatNumber < :seatNumber
//
//    """)
//    boolean existsOverlappingShowtime(@Param("seatNumber") int seatNumber,
//                                      @Param("showtimeId") long showtimeId);

    boolean existsByShowtimeIdAndSeatNumber(long showtimeId, int seatNumber);
}


