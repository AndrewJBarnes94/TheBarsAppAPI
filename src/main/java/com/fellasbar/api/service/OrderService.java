package com.fellasbar.api.service;

import com.fellasbar.api.model.Order;
import com.fellasbar.api.repository.OrderRepository;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionListLineItemsParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void fulfill(String sessionId) {
        if (orderRepository.findByStripeSessionId(sessionId).isPresent()) {
            log.info("Order already fulfilled for session {}, skipping", sessionId);
            return;
        }

        try {
            log.info("Retrieving Stripe session {}", sessionId);
            Session session = Session.retrieve(sessionId);

            log.info("Listing line items for session {}", sessionId);
            var lineItemData = session.listLineItems(SessionListLineItemsParams.builder().build()).getData();

            String lineItemsText = lineItemData.stream()
                .map(li -> "  - " + li.getQuantity() + "x " + li.getDescription()
                           + " — $" + String.format("%.2f", li.getAmountTotal() / 100.0))
                .collect(Collectors.joining("\n"));

            Order order = new Order();
            order.setStripeSessionId(sessionId);
            order.setCustomerEmail(
                session.getCustomerDetails() != null ? session.getCustomerDetails().getEmail() : null
            );
            order.setTotalCents(session.getAmountTotal());
            order.setLineItems(lineItemsText);

            var shipping = session.getShippingDetails();
            if (shipping != null) {
                order.setShippingName(shipping.getName());
                var addr = shipping.getAddress();
                if (addr != null) {
                    order.setShippingLine1(addr.getLine1());
                    order.setShippingLine2(addr.getLine2());
                    order.setShippingCity(addr.getCity());
                    order.setShippingState(addr.getState());
                    order.setShippingPostalCode(addr.getPostalCode());
                    order.setShippingCountry(addr.getCountry());
                }
            }

            orderRepository.save(order);
            log.info("Order saved — session={} customer={} shipping={}",
                sessionId, order.getCustomerEmail(), order.getShippingLine1());

        } catch (Exception e) {
            log.error("Failed to fulfill order for session {}: {}", sessionId, e.getMessage(), e);
        }
    }
}
