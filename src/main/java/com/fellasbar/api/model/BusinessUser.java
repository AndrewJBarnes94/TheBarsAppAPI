package com.fellasbar.api.model;

import jakarta.persistence.*;

@Entity
@Table(name = "business_users")
public class BusinessUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @OneToOne
    @JoinColumn(name = "venue_id", unique = true)
    private Venue venue;

    private boolean active = true;

    public Long getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public Venue getVenue() { return venue; }
    public void setVenue(Venue venue) { this.venue = venue; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
