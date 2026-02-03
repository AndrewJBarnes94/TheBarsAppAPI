package com.fellasbar.api.controller;

import com.fellasbar.api.dto.VenueResponse;
import com.fellasbar.api.service.VenueService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/venues")
public class VenueController {

    private final VenueService venueService;

    public VenueController(VenueService venueService) {
        this.venueService = venueService;
    }

    @GetMapping
    public List<VenueResponse> getAllVenues() {
        return venueService.getAllVenues();
    }

    @GetMapping("/{id}")
    public ResponseEntity<VenueResponse> getVenueById(@PathVariable Long id) {
        return venueService.getVenueById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/nearby")
    public List<VenueResponse> getNearbyVenues(
            @RequestParam double lat,
            @RequestParam double lng,
            @RequestParam(defaultValue = "5") double radiusMiles) {
        return venueService.getNearbyVenues(lat, lng, radiusMiles);
    }

    @GetMapping("/{id}/specials")
    public List<VenueResponse.SpecialResponse> getSpecials(
            @PathVariable Long id,
            @RequestParam(required = false) String day) {
        return venueService.getSpecialsForVenue(id, day);
    }
}
