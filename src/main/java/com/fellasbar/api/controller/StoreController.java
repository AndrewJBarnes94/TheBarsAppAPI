package com.fellasbar.api.controller;

import com.fellasbar.api.service.StripeService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.net.Webhook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class StoreController {

    private static final Logger log = LoggerFactory.getLogger(StoreController.class);

    private final StripeService stripeService;

    @Value("${stripe.webhook-secret}")
    private String webhookSecret;

    @Value("${app.base-url}")
    private String baseUrl;

    public StoreController(StripeService stripeService) {
        this.stripeService = stripeService;
    }

    @PostMapping("/store/checkout")
    public String checkout(
            @RequestParam String productName,
            @RequestParam long priceCents) {
        try {
            String successUrl = baseUrl + "/store/success?session_id={CHECKOUT_SESSION_ID}";
            String cancelUrl = baseUrl + "/store";
            String checkoutUrl = stripeService.createCheckoutSession(productName, priceCents, successUrl, cancelUrl);
            return "redirect:" + checkoutUrl;
        } catch (Exception e) {
            log.error("Stripe checkout error for '{}': {}", productName, e.getMessage());
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
                log.info("Stripe payment completed — session: {}", event.getId());
                // TODO: fulfill order (send confirmation email, update inventory, etc.)
            }
            return ResponseEntity.ok("ok");
        } catch (SignatureVerificationException e) {
            log.warn("Invalid Stripe webhook signature");
            return ResponseEntity.badRequest().body("Invalid signature");
        }
    }
}
