package com.fellasbar.api.controller;

import com.fellasbar.api.model.BusinessUser;
import com.fellasbar.api.repository.BusinessUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class PublicController {

    private static final Logger log = LoggerFactory.getLogger(PublicController.class);
    private final BusinessUserRepository businessUserRepository;

    @Value("#{'${stripe.valid-price-ids}'.split(',')}")
    private List<String> priceIds;

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
    public String store(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAuthenticated = auth != null
                && auth.isAuthenticated()
                && !(auth instanceof AnonymousAuthenticationToken);
        log.debug("Store page auth: {} -> isAuthenticated={}", auth, isAuthenticated);
        model.addAttribute("isAuthenticated", isAuthenticated);
        model.addAttribute("priceIds", priceIds);
        return "store";
    }
}
