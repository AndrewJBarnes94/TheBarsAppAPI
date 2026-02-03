package com.fellasbar.api.repository;

import com.fellasbar.api.model.Special;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpecialRepository extends JpaRepository<Special, Long> {

    List<Special> findByVenueId(Long venueId);

    List<Special> findByVenueIdAndDayOfWeekIgnoreCaseOrVenueIdAndDayOfWeekIgnoreCase(
        Long venueId1, String day,
        Long venueId2, String daily
    );
}
