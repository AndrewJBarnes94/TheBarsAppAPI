package com.fellasbar.api.service;

import com.fellasbar.api.model.Customer;
import com.fellasbar.api.model.Order;
import com.fellasbar.api.repository.CustomerRepository;
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
    private final CustomerRepository customerRepository;
    private final EmailService emailService;

    public OrderService(OrderRepository orderRepository,
                        CustomerRepository customerRepository,
                        EmailService emailService) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.emailService = emailService;
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
            String accountEmail = session.getMetadata() != null ? session.getMetadata().get("accountEmail") : null;
            String stripeEmail = session.getCustomerDetails() != null ? session.getCustomerDetails().getEmail() : null;
            String stripePhone = session.getCustomerDetails() != null ? session.getCustomerDetails().getPhone() : null;
            order.setCustomerEmail(accountEmail != null ? accountEmail : stripeEmail);
            order.setCustomerPhone(stripePhone);
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

            upsertCustomer(order);

            emailService.sendOrderConfirmation(order);
            emailService.sendOrderNotification(order);

        } catch (Exception e) {
            log.error("Failed to fulfill order for session {}: {}", sessionId, e.getMessage(), e);
        }
    }

    private void upsertCustomer(Order order) {
        if (order.getCustomerEmail() == null) return;
        customerRepository.findByEmail(order.getCustomerEmail()).ifPresentOrElse(
            existing -> {
                if (existing.getName() == null) existing.setName(order.getShippingName());
                if (existing.getPhone() == null) existing.setPhone(order.getCustomerPhone());
                customerRepository.save(existing);
            },
            () -> {
                Customer guest = new Customer();
                guest.setEmail(order.getCustomerEmail());
                guest.setName(order.getShippingName());
                guest.setPhone(order.getCustomerPhone());
                customerRepository.save(guest);
            }
        );
    }
}
