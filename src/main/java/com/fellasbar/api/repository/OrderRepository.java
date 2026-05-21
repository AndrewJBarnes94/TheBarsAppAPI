package com.fellasbar.api.repository;

import com.fellasbar.api.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByStripeSessionId(String stripeSessionId);
    List<Order> findAllByCustomerEmailOrderByCreatedAtDesc(String customerEmail);
}
