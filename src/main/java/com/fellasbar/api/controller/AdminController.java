package com.fellasbar.api.controller;

import com.fellasbar.api.model.BusinessUser;
import com.fellasbar.api.model.OperatingHours;
import com.fellasbar.api.model.Order;
import com.fellasbar.api.model.Special;
import com.fellasbar.api.model.TargetVenue;
import com.fellasbar.api.model.Venue;
import com.fellasbar.api.repository.BusinessUserRepository;
import com.fellasbar.api.service.AuditService;
import com.fellasbar.api.service.EmailService;
import com.fellasbar.api.service.VenueService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
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
    private final AuditService auditService;
    private final BusinessUserRepository businessUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public AdminController(VenueService venueService, AuditService auditService,
                           BusinessUserRepository businessUserRepository, PasswordEncoder passwordEncoder,
                           EmailService emailService) {
        this.venueService = venueService;
        this.auditService = auditService;
        this.businessUserRepository = businessUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    @GetMapping("/test-email")
    @ResponseBody
    public ResponseEntity<String> testEmail() {
        Order dummy = new Order();
        dummy.setStripeSessionId("test_session_" + System.currentTimeMillis());
        dummy.setCustomerEmail("test@example.com");
        dummy.setCustomerPhone("555-000-0000");
        dummy.setTotalCents(6500);
        dummy.setLineItems("  - 1x Green T-Shirt (M) — $30.00\n  - 1x Hat — $30.00\n  - Shipping — $5.00");
        dummy.setShippingName("Test Customer");
        dummy.setShippingLine1("123 Test St");
        dummy.setShippingCity("Tampa");
        dummy.setShippingState("FL");
        dummy.setShippingPostalCode("33601");
        dummy.setShippingCountry("US");
        emailService.sendOrderNotification(dummy);
        return ResponseEntity.ok("Test notification sent to store notify address — check Railway logs for result.");
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
                              @RequestParam Map<String, String> params,
                              Principal principal) {
        String username = principal.getName();
        Venue saved = venueService.saveVenue(venue, username);
        saveOperatingHoursFromParams(saved, params, username);
        return "redirect:/admin";
    }

    @GetMapping("/venues/{id}/edit")
    public String editVenueForm(@PathVariable Long id, Model model) {
        Venue venue = venueService.findVenueById(id)
            .orElseThrow(() -> new IllegalArgumentException("Venue not found: " + id));

        venue.setSpecials(venueService.findSpecialsByVenueId(id));

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
                              @RequestParam Map<String, String> params,
                              Principal principal) {
        String username = principal.getName();
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

        venueService.saveVenue(existing, username);
        saveOperatingHoursFromParams(existing, params, username);
        return "redirect:/admin/venues/" + id + "/edit";
    }

    @PostMapping("/venues/{id}/delete")
    public String deleteVenue(@PathVariable Long id, Principal principal) {
        venueService.deleteVenue(id, principal.getName());
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
                                @RequestParam(required = false) String dayOfWeek,
                                @RequestParam(required = false) String startTime,
                                @RequestParam(required = false) String endTime,
                                Principal principal) {
        Venue venue = venueService.findVenueById(venueId)
            .orElseThrow(() -> new IllegalArgumentException("Venue not found: " + venueId));

        Special special = new Special();
        special.setVenue(venue);
        special.setName(name);
        special.setDescription(description);
        special.setOriginalPrice(originalPrice);
        special.setSpecialPrice(specialPrice);
        special.setDayOfWeek(dayOfWeek);
        special.setStartTime(startTime != null && !startTime.isBlank() ? startTime.trim() : null);
        special.setEndTime(endTime != null && !endTime.isBlank() ? endTime.trim() : null);

        venueService.saveSpecial(special, principal.getName());
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
                                @RequestParam(required = false) String dayOfWeek,
                                @RequestParam(required = false) String startTime,
                                @RequestParam(required = false) String endTime,
                                Principal principal) {
        Special special = venueService.findSpecialById(id)
            .orElseThrow(() -> new IllegalArgumentException("Special not found: " + id));

        special.setName(name);
        special.setDescription(description);
        special.setOriginalPrice(originalPrice);
        special.setSpecialPrice(specialPrice);
        special.setDayOfWeek(dayOfWeek);
        special.setStartTime(startTime != null && !startTime.isBlank() ? startTime.trim() : null);
        special.setEndTime(endTime != null && !endTime.isBlank() ? endTime.trim() : null);

        venueService.saveSpecial(special, principal.getName());
        return "redirect:/admin/venues/" + special.getVenue().getId() + "/edit";
    }

    @PostMapping("/specials/{id}/delete")
    public String deleteSpecial(@PathVariable Long id, Principal principal) {
        Special special = venueService.findSpecialById(id)
            .orElseThrow(() -> new IllegalArgumentException("Special not found: " + id));
        Long venueId = special.getVenue().getId();
        venueService.deleteSpecial(id, principal.getName());
        return "redirect:/admin/venues/" + venueId + "/edit";
    }

    // --- Target Venues (crowdsourcing list) ---

    @GetMapping("/targets")
    public String targetVenuesList(Model model) {
        model.addAttribute("targetVenues", venueService.getAllTargetVenues());
        return "admin/target-venues";
    }

    @GetMapping("/targets/{id}/add")
    public String addFromTarget(@PathVariable Long id, Model model) {
        TargetVenue target = venueService.findTargetVenueById(id)
            .orElseThrow(() -> new IllegalArgumentException("Target venue not found: " + id));

        Venue venue = new Venue();
        venue.setName(target.getName());
        venue.setType(target.getType());
        venue.setAddress(target.getAddress());
        venue.setWebsite(target.getWebsite());

        model.addAttribute("venue", venue);
        model.addAttribute("daysOfWeek", DAYS_OF_WEEK);
        model.addAttribute("hoursMap", Map.of());
        model.addAttribute("fromTarget", target.getId());
        return "admin/venue-form";
    }

    // --- Audit Log ---

    @GetMapping("/history")
    public String auditLog(Model model) {
        model.addAttribute("logs", auditService.getAllLogs());
        return "admin/history";
    }

    // --- Business Users ---

    @GetMapping("/business-users")
    public String businessUsers(Model model) {
        model.addAttribute("pendingUsers",
            businessUserRepository.findByStatusOrderByRequestedAtAsc(BusinessUser.Status.PENDING));
        model.addAttribute("activeUsers",
            businessUserRepository.findByStatusNotOrderByNameAsc(BusinessUser.Status.PENDING));
        return "admin/business-users";
    }

    @GetMapping("/business-users/new")
    public String newBusinessUserForm(Model model) {
        model.addAttribute("venues", venueService.findAllVenues());
        return "admin/business-user-form";
    }

    @PostMapping("/business-users")
    public String createBusinessUser(@RequestParam String name,
                                      @RequestParam String email,
                                      @RequestParam String password,
                                      @RequestParam Long venueId,
                                      Principal principal) {
        if (businessUserRepository.existsByEmail(email)) {
            return "redirect:/admin/business-users/new?error=email";
        }
        Venue venue = venueService.findVenueById(venueId)
            .orElseThrow(() -> new IllegalArgumentException("Venue not found: " + venueId));
        BusinessUser user = new BusinessUser();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setVenue(venue);
        businessUserRepository.save(user);
        auditService.log(principal.getName(), "CREATE", "BUSINESS_USER", null,
            "Created business user: " + email + " for venue: " + venue.getName());
        return "redirect:/admin/business-users";
    }

    @GetMapping("/business-users/{id}/approve")
    public String approveBusinessUserForm(@PathVariable Long id, Model model) {
        BusinessUser user = businessUserRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Business user not found: " + id));
        model.addAttribute("user", user);
        model.addAttribute("venues", venueService.findAllVenues());
        return "admin/business-user-approve";
    }

    @PostMapping("/business-users/{id}/approve")
    public String approveBusinessUser(@PathVariable Long id,
                                       @RequestParam String password,
                                       @RequestParam(required = false) Long venueId,
                                       Principal principal) {
        BusinessUser user = businessUserRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Business user not found: " + id));

        user.setPassword(passwordEncoder.encode(password));
        user.setStatus(BusinessUser.Status.ACTIVE);

        if (venueId != null) {
            venueService.findVenueById(venueId).ifPresent(user::setVenue);
        }

        businessUserRepository.save(user);
        auditService.log(principal.getName(), "APPROVE", "BUSINESS_USER", id,
            "Approved portal request for: " + user.getEmail());
        return "redirect:/admin/business-users?approved=1";
    }

    @PostMapping("/business-users/{id}/reject")
    public String rejectBusinessUser(@PathVariable Long id, Principal principal) {
        businessUserRepository.findById(id).ifPresent(user -> {
            auditService.log(principal.getName(), "REJECT", "BUSINESS_USER", id,
                "Rejected portal request for: " + user.getEmail());
            businessUserRepository.delete(user);
        });
        return "redirect:/admin/business-users?rejected=1";
    }

    @PostMapping("/business-users/{id}/delete")
    public String deleteBusinessUser(@PathVariable Long id, Principal principal) {
        businessUserRepository.findById(id).ifPresent(user -> {
            auditService.log(principal.getName(), "DELETE", "BUSINESS_USER", id,
                "Deleted business user: " + user.getEmail());
            businessUserRepository.delete(user);
        });
        return "redirect:/admin/business-users";
    }

    // --- Helpers ---

    private void saveOperatingHoursFromParams(Venue venue, Map<String, String> params, String username) {
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
        if (!hoursList.isEmpty()) {
            venueService.saveOperatingHours(venue, hoursList);
            auditService.log(username, "UPDATE", "OPERATING_HOURS", venue.getId(),
                "Updated operating hours for: " + venue.getName());
        }
    }
}
