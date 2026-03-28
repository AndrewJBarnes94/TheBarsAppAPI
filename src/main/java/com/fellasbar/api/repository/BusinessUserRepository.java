package com.fellasbar.api.repository;

import com.fellasbar.api.model.BusinessUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BusinessUserRepository extends JpaRepository<BusinessUser, Long> {
    Optional<BusinessUser> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByVenueId(Long venueId);
    List<BusinessUser> findAllByOrderByNameAsc();
}
