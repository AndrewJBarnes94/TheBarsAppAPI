package com.fellasbar.api.repository;

import com.fellasbar.api.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    Optional<Inventory> findByPriceId(String priceId);
}
