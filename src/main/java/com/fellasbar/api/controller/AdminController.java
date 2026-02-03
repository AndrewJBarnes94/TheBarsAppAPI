package com.fellasbar.api.controller;

import com.fellasbar.api.model.OperatingHours;
import com.fellasbar.api.model.Special;
import com.fellasbar.api.model.Venue;
import com.fellasbar.api.service.VenueService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private static final List<String> DAYS_OF_WEEK = Arrays.asList(
        "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"
    );

    private final VenueService venueService;

    public AdminController(VenueService venueService) {
        this.venueService = venueService;
    }

    // --- Dashboard ---

    @GetMapping
    public String dashboard(Model model) {
        model.addAttribute("venues", venueService.findAllVenues());
        return "admin/dashboard";
    }

    // --- Venue CRUD ---

    @GetMapping("/venues/new")
    public String newVenueForm(Model model) {
        model.addAttribute("venue", new Venue());
        model.addAttribute("daysOfWeek", DAYS_OF_WEEK);
        model.addAttribute("hoursMap", Map.of());
        return "admin/venue-form";
    }

    @PostMapping("/venues")
    public String createVenue(@ModelAttribute Venue venue,
                              @RequestParam Map<String, String> params) {
        Venue saved = venueService.saveVenue(venue);
        saveOperatingHoursFromParams(saved, params);
        return "redirect:/admin";
    }

    @GetMapping("/venues/{id}/edit")
    public String editVenueForm(@PathVariable Long id, Model model) {
        Venue venue = venueService.findVenueById(id)
            .orElseThrow(() -> new IllegalArgumentException("Venue not found: " + id));

        List<OperatingHours> hours = venueService.findOperatingHoursByVenueId(id);
        Map<String, String> hoursMap = hours.stream()
            .collect(Collectors.toMap(OperatingHours::getDayOfWeek, OperatingHours::getHours,
                (a, b) -> a));

        model.addAttribute("venue", venue);
        model.addAttribute("daysOfWeek", DAYS_OF_WEEK);
        model.addAttribute("hoursMap", hoursMap);
        return "admin/venue-form";
    }

    @PostMapping("/venues/{id}")
    public String updateVenue(@PathVariable Long id,
                              @ModelAttribute Venue formVenue,
                              @RequestParam Map<String, String> params) {
        Venue existing = venueService.findVenueById(id)
            .orElseThrow(() -> new IllegalArgumentException("Venue not found: " + id));

        existing.setName(formVenue.getName());
        existing.setType(formVenue.getType());
        existing.setAddress(formVenue.getAddress());
        existing.setLatitude(formVenue.getLatitude());
        existing.setLongitude(formVenue.getLongitude());
        existing.setPhone(formVenue.getPhone());
        existing.setWebsite(formVenue.getWebsite());
        existing.setDescription(formVenue.getDescription());
        existing.setImageUrl(formVenue.getImageUrl());
        existing.setRating(formVenue.getRating());
        existing.setReviewCount(formVenue.getReviewCount());
        existing.setIsOpen(formVenue.getIsOpen());

        venueService.saveVenue(existing);
        saveOperatingHoursFromParams(existing, params);
        return "redirect:/admin/venues/" + id + "/edit";
    }

    @PostMapping("/venues/{id}/delete")
    public String deleteVenue(@PathVariable Long id) {
        venueService.deleteVenue(id);
        return "redirect:/admin";
    }

    // --- Special CRUD ---

    @GetMapping("/venues/{venueId}/specials/new")
    public String newSpecialForm(@PathVariable Long venueId, Model model) {
        Venue venue = venueService.findVenueById(venueId)
            .orElseThrow(() -> new IllegalArgumentException("Venue not found: " + venueId));
        Special special = new Special();
        special.setVenue(venue);

        model.addAttribute("special", special);
        model.addAttribute("venue", venue);
        model.addAttribute("daysOfWeek", DAYS_OF_WEEK);
        return "admin/special-form";
    }

    @PostMapping("/venues/{venueId}/specials")
    public String createSpecial(@PathVariable Long venueId,
                                @RequestParam String name,
                                @RequestParam(required = false) String description,
                                @RequestParam(required = false) BigDecimal originalPrice,
                                @RequestParam(required = false) BigDecimal specialPrice,
                                @RequestParam(required = false) String dayOfWeek) {
        Venue venue = venueService.findVenueById(venueId)
            .orElseThrow(() -> new IllegalArgumentException("Venue not found: " + venueId));

        Special special = new Special();
        special.setVenue(venue);
        special.setName(name);
        special.setDescription(description);
        special.setOriginalPrice(originalPrice);
        special.setSpecialPrice(specialPrice);
        special.setDayOfWeek(dayOfWeek);

        venueService.saveSpecial(special);
        return "redirect:/admin/venues/" + venueId + "/edit";
    }

    @GetMapping("/specials/{id}/edit")
    public String editSpecialForm(@PathVariable Long id, Model model) {
        Special special = venueService.findSpecialById(id)
            .orElseThrow(() -> new IllegalArgumentException("Special not found: " + id));

        model.addAttribute("special", special);
        model.addAttribute("venue", special.getVenue());
        model.addAttribute("daysOfWeek", DAYS_OF_WEEK);
        return "admin/special-form";
    }

    @PostMapping("/specials/{id}")
    public String updateSpecial(@PathVariable Long id,
                                @RequestParam String name,
                                @RequestParam(required = false) String description,
                                @RequestParam(required = false) BigDecimal originalPrice,
                                @RequestParam(required = false) BigDecimal specialPrice,
                                @RequestParam(required = false) String dayOfWeek) {
        Special special = venueService.findSpecialById(id)
            .orElseThrow(() -> new IllegalArgumentException("Special not found: " + id));

        special.setName(name);
        special.setDescription(description);
        special.setOriginalPrice(originalPrice);
        special.setSpecialPrice(specialPrice);
        special.setDayOfWeek(dayOfWeek);

        venueService.saveSpecial(special);
        return "redirect:/admin/venues/" + special.getVenue().getId() + "/edit";
    }

    @PostMapping("/specials/{id}/delete")
    public String deleteSpecial(@PathVariable Long id) {
        Special special = venueService.findSpecialById(id)
            .orElseThrow(() -> new IllegalArgumentException("Special not found: " + id));
        Long venueId = special.getVenue().getId();
        venueService.deleteSpecial(id);
        return "redirect:/admin/venues/" + venueId + "/edit";
    }

    // --- Login page ---

    @Controller
    static class LoginController {
        @GetMapping("/")
        public String home() {
            return "redirect:/admin";
        }

        @GetMapping("/login")
        public String login() {
            return "admin/login";
        }
    }

    // --- Helpers ---

    private void saveOperatingHoursFromParams(Venue venue, Map<String, String> params) {
        List<OperatingHours> hoursList = new ArrayList<>();
        for (String day : DAYS_OF_WEEK) {
            String value = params.get("hours_" + day);
            if (value != null && !value.isBlank()) {
                OperatingHours oh = new OperatingHours();
                oh.setDayOfWeek(day);
                oh.setHours(value.trim());
                hoursList.add(oh);
            }
        }
        venueService.saveOperatingHours(venue, hoursList);
    }
}
