package com.fellasbar.api.controller;

import com.fellasbar.api.model.BusinessUser;
import com.fellasbar.api.model.OperatingHours;
import com.fellasbar.api.model.Special;
import com.fellasbar.api.model.Venue;
import com.fellasbar.api.repository.BusinessUserRepository;
import com.fellasbar.api.service.VenueService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/portal")
public class PortalController {

    private static final List<String> DAYS_OF_WEEK = Arrays.asList(
        "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"
    );

    private final BusinessUserRepository businessUserRepository;
    private final VenueService venueService;

    public PortalController(BusinessUserRepository businessUserRepository, VenueService venueService) {
        this.businessUserRepository = businessUserRepository;
        this.venueService = venueService;
    }

    private BusinessUser currentUser(Principal principal) {
        return businessUserRepository.findByEmail(principal.getName())
            .orElseThrow(() -> new IllegalStateException("Business user not found"));
    }

    // --- Login ---

    @GetMapping("/login")
    public String login() {
        return "portal/login";
    }

    // --- Dashboard ---

    @GetMapping("/dashboard")
    public String dashboard(Model model, Principal principal) {
        BusinessUser user = currentUser(principal);
        Venue venue = user.getVenue();
        List<OperatingHours> hours = venueService.findOperatingHoursByVenueId(venue.getId());
        List<Special> specials = venueService.findSpecialsByVenueId(venue.getId());
        model.addAttribute("user", user);
        model.addAttribute("venue", venue);
        model.addAttribute("hoursCount", hours.size());
        model.addAttribute("specialsCount", specials.size());
        return "portal/dashboard";
    }

    // --- Profile ---

    @GetMapping("/profile")
    public String profileForm(Model model, Principal principal) {
        model.addAttribute("venue", currentUser(principal).getVenue());
        model.addAttribute("saved", false);
        return "portal/profile";
    }

    @PostMapping("/profile")
    public String saveProfile(@RequestParam String name,
                               @RequestParam(required = false) String phone,
                               @RequestParam(required = false) String website,
                               @RequestParam(required = false) String description,
                               @RequestParam(required = false) String imageUrl,
                               Principal principal) {
        BusinessUser user = currentUser(principal);
        Venue venue = user.getVenue();
        venue.setName(name);
        venue.setPhone(phone);
        venue.setWebsite(website);
        venue.setDescription(description);
        venue.setImageUrl(imageUrl);
        venueService.saveVenue(venue, user.getEmail());
        return "redirect:/portal/profile?saved=true";
    }

    // --- Hours ---

    @GetMapping("/hours")
    public String hoursForm(Model model, Principal principal) {
        Venue venue = currentUser(principal).getVenue();
        List<OperatingHours> hours = venueService.findOperatingHoursByVenueId(venue.getId());
        Map<String, String> hoursMap = hours.stream()
            .collect(Collectors.toMap(OperatingHours::getDayOfWeek, OperatingHours::getHours, (a, b) -> a));
        model.addAttribute("daysOfWeek", DAYS_OF_WEEK);
        model.addAttribute("hoursMap", hoursMap);
        return "portal/hours";
    }

    @PostMapping("/hours")
    public String saveHours(@RequestParam Map<String, String> params, Principal principal) {
        BusinessUser user = currentUser(principal);
        Venue venue = user.getVenue();
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
        return "redirect:/portal/hours?saved=true";
    }

    // --- Specials ---

    @GetMapping("/specials")
    public String specials(Model model, Principal principal) {
        Venue venue = currentUser(principal).getVenue();
        model.addAttribute("venue", venue);
        model.addAttribute("specials", venueService.findSpecialsByVenueId(venue.getId()));
        model.addAttribute("daysOfWeek", DAYS_OF_WEEK);
        return "portal/specials";
    }

    @PostMapping("/specials")
    public String createSpecial(@RequestParam String name,
                                 @RequestParam(required = false) String description,
                                 @RequestParam(required = false) BigDecimal originalPrice,
                                 @RequestParam(required = false) BigDecimal specialPrice,
                                 @RequestParam(required = false) String dayOfWeek,
                                 @RequestParam(required = false) String startTime,
                                 @RequestParam(required = false) String endTime,
                                 Principal principal) {
        BusinessUser user = currentUser(principal);
        Venue venue = user.getVenue();
        Special special = new Special();
        special.setVenue(venue);
        special.setName(name);
        special.setDescription(description);
        special.setOriginalPrice(originalPrice);
        special.setSpecialPrice(specialPrice);
        special.setDayOfWeek(dayOfWeek);
        special.setStartTime(startTime != null && !startTime.isBlank() ? startTime.trim() : null);
        special.setEndTime(endTime != null && !endTime.isBlank() ? endTime.trim() : null);
        venueService.saveSpecial(special, user.getEmail());
        return "redirect:/portal/specials";
    }

    @PostMapping("/specials/{id}/delete")
    public String deleteSpecial(@PathVariable Long id, Principal principal) {
        BusinessUser user = currentUser(principal);
        Special special = venueService.findSpecialById(id)
            .orElseThrow(() -> new IllegalArgumentException("Special not found: " + id));
        // ensure they only delete their own specials
        if (!special.getVenue().getId().equals(user.getVenue().getId())) {
            return "redirect:/portal/specials";
        }
        venueService.deleteSpecial(id, user.getEmail());
        return "redirect:/portal/specials";
    }
}
