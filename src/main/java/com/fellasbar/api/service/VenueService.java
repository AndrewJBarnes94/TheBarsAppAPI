package com.fellasbar.api.service;

import com.fellasbar.api.dto.VenueResponse;
import com.fellasbar.api.model.OperatingHours;
import com.fellasbar.api.model.Special;
import com.fellasbar.api.model.Venue;
import com.fellasbar.api.repository.OperatingHoursRepository;
import com.fellasbar.api.repository.SpecialRepository;
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

    public VenueService(VenueRepository venueRepository,
                        SpecialRepository specialRepository,
                        OperatingHoursRepository operatingHoursRepository) {
        this.venueRepository = venueRepository;
        this.specialRepository = specialRepository;
        this.operatingHoursRepository = operatingHoursRepository;
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
    public Venue saveVenue(Venue venue) {
        return venueRepository.save(venue);
    }

    @Transactional
    public void deleteVenue(Long id) {
        venueRepository.deleteById(id);
    }

    public Optional<Special> findSpecialById(Long id) {
        return specialRepository.findById(id);
    }

    @Transactional
    public Special saveSpecial(Special special) {
        return specialRepository.save(special);
    }

    @Transactional
    public void deleteSpecial(Long id) {
        specialRepository.deleteById(id);
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
