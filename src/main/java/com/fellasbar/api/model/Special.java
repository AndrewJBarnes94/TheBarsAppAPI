package com.fellasbar.api.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "specials")
public class Special {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "venue_id", nullable = false)
    private Venue venue;

    private String name;

    @Column(length = 500)
    private String description;

    @Column(name = "original_price", precision = 10, scale = 2)
    private BigDecimal originalPrice;

    @Column(name = "special_price", precision = 10, scale = 2)
    private BigDecimal specialPrice;

    @Column(name = "day_of_week", length = 20)
    private String dayOfWeek;

    public Special() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Venue getVenue() { return venue; }
    public void setVenue(Venue venue) { this.venue = venue; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public BigDecimal getOriginalPrice() { return originalPrice; }
    public void setOriginalPrice(BigDecimal originalPrice) { this.originalPrice = originalPrice; }

    public BigDecimal getSpecialPrice() { return specialPrice; }
    public void setSpecialPrice(BigDecimal specialPrice) { this.specialPrice = specialPrice; }

    public String getDayOfWeek() { return dayOfWeek; }
    public void setDayOfWeek(String dayOfWeek) { this.dayOfWeek = dayOfWeek; }
}
