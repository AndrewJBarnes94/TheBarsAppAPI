package com.fellasbar.api.service;

import com.fellasbar.api.dto.CartItem;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StripeService {

    @Value("${stripe.secret-key}")
    private String secretKey;

    @PostConstruct
    public void init() {
        Stripe.apiKey = secretKey;
    }

    public String createCheckoutSession(List<CartItem> items, String successUrl, String cancelUrl, String customerEmail) throws StripeException {
        SessionCreateParams.Builder builder = SessionCreateParams.builder()
            .setMode(SessionCreateParams.Mode.PAYMENT)
            .setSuccessUrl(successUrl)
            .setCancelUrl(cancelUrl);

        if (customerEmail != null) {
            builder.setCustomerEmail(customerEmail);
            builder.putMetadata("accountEmail", customerEmail);
        }

        builder.setPhoneNumberCollection(
                SessionCreateParams.PhoneNumberCollection.builder()
                    .setEnabled(true)
                    .build()
            );

        builder.setShippingAddressCollection(
                SessionCreateParams.ShippingAddressCollection.builder()
                    .addAllowedCountry(SessionCreateParams.ShippingAddressCollection.AllowedCountry.US)
                    .build()
            );

        builder.addShippingOption(
                SessionCreateParams.ShippingOption.builder()
                    .setShippingRateData(
                        SessionCreateParams.ShippingOption.ShippingRateData.builder()
                            .setDisplayName("Standard Shipping")
                            .setType(SessionCreateParams.ShippingOption.ShippingRateData.Type.FIXED_AMOUNT)
                            .setFixedAmount(
                                SessionCreateParams.ShippingOption.ShippingRateData.FixedAmount.builder()
                                    .setAmount(500L)
                                    .setCurrency("usd")
                                    .build()
                            )
                            .build()
                    )
                    .build()
            );

        for (CartItem item : items) {
            builder.addLineItem(
                SessionCreateParams.LineItem.builder()
                    .setQuantity((long) item.quantity())
                    .setPrice(item.priceId())
                    .build()
            );
        }

        Session session = Session.create(builder.build());
        return session.getUrl();
    }

    public String getCustomerEmail(String sessionId) throws StripeException {
        Session session = Session.retrieve(sessionId);
        return session.getCustomerDetails() != null ? session.getCustomerDetails().getEmail() : null;
    }
}
