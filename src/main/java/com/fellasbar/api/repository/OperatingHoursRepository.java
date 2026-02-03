package com.fellasbar.api.repository;

import com.fellasbar.api.model.OperatingHours;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OperatingHoursRepository extends JpaRepository<OperatingHours, Long> {

    List<OperatingHours> findByVenueId(Long venueId);

    @Modifying
    @Query("DELETE FROM OperatingHours oh WHERE oh.venue.id = ?1")
    void deleteByVenueId(Long venueId);
}
