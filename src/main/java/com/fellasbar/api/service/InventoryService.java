package com.fellasbar.api.service;

import com.fellasbar.api.model.Inventory;
import com.fellasbar.api.repository.InventoryRepository;
import com.stripe.model.LineItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class InventoryService {

    private static final Logger log = LoggerFactory.getLogger(InventoryService.class);

    private final InventoryRepository inventoryRepository;

    public InventoryService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    @Transactional
    public void decrementForLineItems(List<LineItem> lineItems) {
        for (LineItem li : lineItems) {
            if (li.getPrice() == null) continue;
            String priceId = li.getPrice().getId();
            int qty = li.getQuantity() != null ? li.getQuantity().intValue() : 1;
            inventoryRepository.findByPriceId(priceId).ifPresent(inv -> {
                int remaining = Math.max(0, inv.getQuantity() - qty);
                inv.setQuantity(remaining);
                inventoryRepository.save(inv);
                log.info("Inventory updated: priceId={} ordered={} remaining={}", priceId, qty, remaining);
            });
        }
    }

    public Set<String> getSoldOutPriceIds() {
        return inventoryRepository.findAll().stream()
            .filter(inv -> inv.getQuantity() <= 0)
            .map(Inventory::getPriceId)
            .collect(Collectors.toSet());
    }
}
