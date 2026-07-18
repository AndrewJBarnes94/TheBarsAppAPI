package com.fellasbar.api.model;

import jakarta.persistence.*;

@Entity
@Table(name = "inventory")
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String priceId;

    @Column(nullable = false)
    private int quantity;

    public Inventory() {}

    public Inventory(String priceId, int quantity) {
        this.priceId = priceId;
        this.quantity = quantity;
    }

    public Long getId() { return id; }
    public String getPriceId() { return priceId; }
    public void setPriceId(String priceId) { this.priceId = priceId; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}
