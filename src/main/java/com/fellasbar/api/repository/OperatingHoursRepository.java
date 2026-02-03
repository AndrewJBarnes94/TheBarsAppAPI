package com.fellasbar.api.repository;

import com.fellasbar.api.model.OperatingHours;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OperatingHoursRepository extends JpaRepository<OperatingHours, Long> {

    List<OperatingHours> findByVenueId(Long venueId);

    void deleteByVenueId(Long venueId);
}
