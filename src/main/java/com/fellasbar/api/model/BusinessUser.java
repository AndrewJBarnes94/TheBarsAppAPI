package com.fellasbar.api.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "business_users")
public class BusinessUser {

    public enum Status { PENDING, ACTIVE, INACTIVE }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    private String password;

    @OneToOne
    @JoinColumn(name = "venue_id", unique = true)
    private Venue venue;

    private String venueName;

    @Enumerated(EnumType.STRING)
    private Status status = Status.ACTIVE;

    private Instant requestedAt;

    @PrePersist
    protected void onCreate() {
        if (requestedAt == null) requestedAt = Instant.now();
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public Venue getVenue() { return venue; }
    public void setVenue(Venue venue) { this.venue = venue; }
    public String getVenueName() { return venueName; }
    public void setVenueName(String venueName) { this.venueName = venueName; }
    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }
    public Instant getRequestedAt() { return requestedAt; }
    public boolean isActive() { return status == Status.ACTIVE; }
}
