package com.fellasbar.api.service;

import com.fellasbar.api.dto.VenueResponse;
import com.fellasbar.api.model.OperatingHours;
import com.fellasbar.api.model.Special;
import com.fellasbar.api.model.TargetVenue;
import com.fellasbar.api.model.Venue;
import com.fellasbar.api.repository.OperatingHoursRepository;
import com.fellasbar.api.repository.SpecialRepository;
import com.fellasbar.api.repository.TargetVenueRepository;
import com.fellasbar.api.repository.VenueRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class VenueService {

    private final VenueRepository venueRepository;
    private final SpecialRepository specialRepository;
    private final OperatingHoursRepository operatingHoursRepository;
    private final TargetVenueRepository targetVenueRepository;
    private final AuditService auditService;

    public VenueService(VenueRepository venueRepository,
                        SpecialRepository specialRepository,
                        OperatingHoursRepository operatingHoursRepository,
                        TargetVenueRepository targetVenueRepository,
                        AuditService auditService) {
        this.venueRepository = venueRepository;
        this.specialRepository = specialRepository;
        this.operatingHoursRepository = operatingHoursRepository;
        this.targetVenueRepository = targetVenueRepository;
        this.auditService = auditService;
    }

    public List<VenueResponse> getAllVenues() {
        return venueRepository.findAll().stream()
            .map(VenueResponse::from)
            .toList();
    }

    public Optional<VenueResponse> getVenueById(Long id) {
        return venueRepository.findById(id)
            .map(VenueResponse::from);
    }

    public List<VenueResponse> getNearbyVenues(double lat, double lng, double radiusMiles) {
        return venueRepository.findNearby(lat, lng, radiusMiles).stream()
            .map(VenueResponse::from)
            .toList();
    }

    public List<VenueResponse.SpecialResponse> getSpecialsForVenue(Long venueId, String day) {
        List<Special> specials;
        if (day != null && !day.isBlank()) {
            specials = specialRepository
                .findByVenueIdAndDayOfWeekIgnoreCaseOrVenueIdAndDayOfWeekIgnoreCase(
                    venueId, day, venueId, "Daily"
                );
        } else {
            specials = specialRepository.findByVenueId(venueId);
        }
        return specials.stream()
            .map(VenueResponse.SpecialResponse::from)
            .toList();
    }

    // --- Admin write methods ---

    public List<Venue> findAllVenues() {
        return venueRepository.findAllWithSpecials();
    }

    public Optional<Venue> findVenueById(Long id) {
        return venueRepository.findById(id);
    }

    @Transactional
    public Venue saveVenue(Venue venue, String username) {
        boolean isNew = venue.getId() == null;
        Venue saved = venueRepository.save(venue);

        if (isNew) {
            auditService.log(username, "CREATE", "VENUE", saved.getId(), "Created venue: " + saved.getName());
            // Mark matching target venue as added
            targetVenueRepository.findByNameIgnoreCase(saved.getName()).ifPresent(target -> {
                target.setIsAdded(true);
                target.setAddedBy(username);
                targetVenueRepository.save(target);
            });
        } else {
            auditService.log(username, "UPDATE", "VENUE", saved.getId(), "Updated venue: " + saved.getName());
        }

        return saved;
    }

    @Transactional
    public void deleteVenue(Long id, String username) {
        venueRepository.findById(id).ifPresent(venue -> {
            auditService.log(username, "DELETE", "VENUE", id, "Deleted venue: " + venue.getName());
            // Unmark matching target venue
            targetVenueRepository.findByNameIgnoreCase(venue.getName()).ifPresent(target -> {
                target.setIsAdded(false);
                target.setAddedBy(null);
                targetVenueRepository.save(target);
            });
        });
        venueRepository.deleteById(id);
    }

    public Optional<Special> findSpecialById(Long id) {
        return specialRepository.findById(id);
    }

    public List<Special> findSpecialsByVenueId(Long venueId) {
        return specialRepository.findByVenueId(venueId);
    }

    @Transactional
    public Special saveSpecial(Special special, String username) {
        boolean isNew = special.getId() == null;
        Special saved = specialRepository.save(special);

        if (isNew) {
            auditService.log(username, "CREATE", "SPECIAL", saved.getId(),
                "Created special: " + saved.getName() + " for venue: " + saved.getVenue().getName());
        } else {
            auditService.log(username, "UPDATE", "SPECIAL", saved.getId(),
                "Updated special: " + saved.getName() + " for venue: " + saved.getVenue().getName());
        }

        return saved;
    }

    @Transactional
    public void deleteSpecial(Long id, String username) {
        specialRepository.findById(id).ifPresent(special -> {
            auditService.log(username, "DELETE", "SPECIAL", id,
                "Deleted special: " + special.getName() + " from venue: " + special.getVenue().getName());
        });
        specialRepository.deleteById(id);
    }

    // --- Target Venue methods ---

    public List<TargetVenue> getAllTargetVenues() {
        return targetVenueRepository.findAllByOrderByIsAddedAscNameAsc();
    }

    public List<TargetVenue> getAvailableTargetVenues() {
        return targetVenueRepository.findByIsAddedFalseOrderByNameAsc();
    }

    public Optional<TargetVenue> findTargetVenueById(Long id) {
        return targetVenueRepository.findById(id);
    }

    public List<OperatingHours> findOperatingHoursByVenueId(Long venueId) {
        return operatingHoursRepository.findByVenueId(venueId);
    }

    @Transactional
    public void saveOperatingHours(Venue venue, List<OperatingHours> hoursList) {
        operatingHoursRepository.deleteByVenueId(venue.getId());
        for (OperatingHours oh : hoursList) {
            oh.setVenue(venue);
            operatingHoursRepository.save(oh);
        }
    }
}
