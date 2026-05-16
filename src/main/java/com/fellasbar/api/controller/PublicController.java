package com.fellasbar.api.controller;

import com.fellasbar.api.model.BusinessUser;
import com.fellasbar.api.repository.BusinessUserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PublicController {

    private final BusinessUserRepository businessUserRepository;

    public PublicController(BusinessUserRepository businessUserRepository) {
        this.businessUserRepository = businessUserRepository;
    }

    @PostMapping("/listing-request")
    public String listingRequest(@RequestParam String name,
                                  @RequestParam String email,
                                  @RequestParam String venueName) {
        if (businessUserRepository.existsByEmail(email)) {
            return "redirect:/thebarsapp?request=duplicate";
        }
        BusinessUser request = new BusinessUser();
        request.setName(name);
        request.setEmail(email);
        request.setVenueName(venueName);
        request.setStatus(BusinessUser.Status.PENDING);
        businessUserRepository.save(request);
        return "redirect:/thebarsapp?request=sent";
    }

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/login")
    public String login() {
        return "admin/login";
    }

    @GetMapping("/thebarsapp")
    public String barsApp() {
        return "thebarsapp";
    }

    @GetMapping("/store")
    public String store() {
        return "store";
    }
}
