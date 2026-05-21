package com.fellasbar.api.controller;

import com.fellasbar.api.dto.CartItem;
import com.fellasbar.api.service.OrderService;
import com.fellasbar.api.service.StripeService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.net.Webhook;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Controller
public class StoreController {

    private static final Logger log = LoggerFactory.getLogger(StoreController.class);

    private final StripeService stripeService;
    private final OrderService orderService;

    @Value("${stripe.webhook-secret}")
    private String webhookSecret;

    @Value("${app.base-url}")
    private String baseUrl;

    @Value("#{'${stripe.valid-price-ids}'.split(',')}")
    private List<String> validPriceIdList;

    public StoreController(StripeService stripeService, OrderService orderService) {
        this.stripeService = stripeService;
        this.orderService = orderService;
    }

    @PostMapping("/store/save-cart")
    @ResponseBody
    public ResponseEntity<?> saveCart(@RequestBody List<CartItem> items, HttpSession session) {
        Set<String> validPriceIds = new HashSet<>(validPriceIdList);
        boolean allValid = items != null && !items.isEmpty() &&
            items.stream().allMatch(i -> validPriceIds.contains(i.priceId()) && i.quantity() > 0);

        if (!allValid) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid items"));
        }

        session.setAttribute("pendingCart", items);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/store/initiate-checkout")
    public String initiateCheckout(HttpSession session, Authentication principal) {
        @SuppressWarnings("unchecked")
        List<CartItem> items = (List<CartItem>) session.getAttribute("pendingCart");

        if (items == null || items.isEmpty()) {
            return "redirect:/store";
        }

        Set<String> validPriceIds = new HashSet<>(validPriceIdList);
        boolean allValid = items.stream().allMatch(i -> validPriceIds.contains(i.priceId()) && i.quantity() > 0);
        if (!allValid) {
            session.removeAttribute("pendingCart");
            return "redirect:/store?error=true";
        }

        try {
            String email = principal != null ? principal.getName() : null;
            String successUrl = baseUrl + "/store/success?session_id={CHECKOUT_SESSION_ID}";
            String cancelUrl = baseUrl + "/store";
            String checkoutUrl = stripeService.createCheckoutSession(items, successUrl, cancelUrl, email);
            session.removeAttribute("pendingCart");
            return "redirect:" + checkoutUrl;
        } catch (Exception e) {
            log.error("Stripe checkout error: {}", e.getMessage());
            return "redirect:/store?error=true";
        }
    }

    @GetMapping("/store/success")
    public String success() {
        return "store-success";
    }

    @PostMapping("/webhooks/stripe")
    @ResponseBody
    public ResponseEntity<String> webhook(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String sigHeader) {
        try {
            Event event = Webhook.constructEvent(payload, sigHeader, webhookSecret);
            if ("checkout.session.completed".equals(event.getType())) {
                String sessionId = null;
                var deserializer = event.getDataObjectDeserializer();
                if (deserializer.getObject().isPresent()) {
                    sessionId = ((com.stripe.model.checkout.Session) deserializer.getObject().get()).getId();
                } else {
                    // Fallback for API version mismatches — parse ID from raw JSON
                    try {
                        String raw = deserializer.getRawJson();
                        sessionId = new com.fasterxml.jackson.databind.ObjectMapper()
                            .readTree(raw).get("id").asText();
                    } catch (Exception ex) {
                        log.error("Could not extract session ID from event: {}", ex.getMessage());
                    }
                }
                log.info("checkout.session.completed received, sessionId={}", sessionId);
                if (sessionId != null) {
                    orderService.fulfill(sessionId);
                }
            }
            return ResponseEntity.ok("ok");
        } catch (SignatureVerificationException e) {
            log.warn("Invalid Stripe webhook signature");
            return ResponseEntity.badRequest().body("Invalid signature");
        }
    }
}
