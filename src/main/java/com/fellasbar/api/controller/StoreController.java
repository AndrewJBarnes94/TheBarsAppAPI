package com.fellasbar.api.controller;

import com.fellasbar.api.dto.CartItem;
import com.fellasbar.api.service.OrderService;
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

import java.util.List;
import java.util.Map;

@Controller
public class StoreController {

    private static final Logger log = LoggerFactory.getLogger(StoreController.class);

    private final StripeService stripeService;
    private final OrderService orderService;

    @Value("${stripe.webhook-secret}")
    private String webhookSecret;

    @Value("${app.base-url}")
    private String baseUrl;

    public StoreController(StripeService stripeService, OrderService orderService) {
        this.stripeService = stripeService;
        this.orderService = orderService;
    }

    @PostMapping("/store/checkout")
    @ResponseBody
    public ResponseEntity<?> checkout(@RequestBody List<CartItem> items) {
        try {
            String successUrl = baseUrl + "/store/success?session_id={CHECKOUT_SESSION_ID}";
            String cancelUrl = baseUrl + "/store";
            String checkoutUrl = stripeService.createCheckoutSession(items, successUrl, cancelUrl);
            return ResponseEntity.ok(Map.of("url", checkoutUrl));
        } catch (Exception e) {
            log.error("Stripe checkout error: {}", e.getMessage());
            return ResponseEntity.internalServerError().body(Map.of("error", "Checkout failed"));
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
                        sessionId = com.google.gson.JsonParser.parseString(raw)
                            .getAsJsonObject().get("id").getAsString();
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
