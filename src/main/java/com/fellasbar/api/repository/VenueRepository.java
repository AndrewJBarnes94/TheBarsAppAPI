package com.fellasbar.api.repository;

import com.fellasbar.api.model.Venue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VenueRepository extends JpaRepository<Venue, Long> {

    @Query("SELECT DISTINCT v FROM Venue v LEFT JOIN FETCH v.specials")
    List<Venue> findAllWithSpecials();

    @Query("""
        SELECT v FROM Venue v
        WHERE (6371 * acos(
            cos(radians(:lat)) * cos(radians(v.latitude))
            * cos(radians(v.longitude) - radians(:lng))
            + sin(radians(:lat)) * sin(radians(v.latitude))
        )) <= :radiusMiles
        ORDER BY (6371 * acos(
            cos(radians(:lat)) * cos(radians(v.latitude))
            * cos(radians(v.longitude) - radians(:lng))
            + sin(radians(:lat)) * sin(radians(v.latitude))
        ))
    """)
    List<Venue> findNearby(
        @Param("lat") double lat,
        @Param("lng") double lng,
        @Param("radiusMiles") double radiusMiles
    );
}
