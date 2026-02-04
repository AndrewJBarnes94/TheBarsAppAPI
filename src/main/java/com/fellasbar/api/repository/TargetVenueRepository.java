package com.fellasbar.api.repository;

import com.fellasbar.api.model.TargetVenue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TargetVenueRepository extends JpaRepository<TargetVenue, Long> {

    List<TargetVenue> findAllByOrderByIsAddedAscNameAsc();

    List<TargetVenue> findByIsAddedFalseOrderByNameAsc();

    Optional<TargetVenue> findByNameIgnoreCase(String name);
}
