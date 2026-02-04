package com.fellasbar.api.model;

import jakarta.persistence.*;

@Entity
@Table(name = "target_venues")
public class TargetVenue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(length = 50)
    private String type; // Bar, Brewery, Restaurant, etc.

    @Column(length = 500)
    private String address;

    @Column(length = 500)
    private String website;

    @Column(nullable = false)
    private Boolean isAdded = false;

    private String addedBy; // username who added it to venues

    public TargetVenue() {}

    public TargetVenue(String name, String type, String address, String website) {
        this.name = name;
        this.type = type;
        this.address = address;
        this.website = website;
        this.isAdded = false;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getWebsite() { return website; }
    public void setWebsite(String website) { this.website = website; }

    public Boolean getIsAdded() { return isAdded; }
    public void setIsAdded(Boolean isAdded) { this.isAdded = isAdded; }

    public String getAddedBy() { return addedBy; }
    public void setAddedBy(String addedBy) { this.addedBy = addedBy; }
}
