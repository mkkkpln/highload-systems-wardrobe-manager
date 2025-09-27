package com.example.highloadsystemswardrobemanager.entity;

import com.example.highloadsystemswardrobemanager.entity.enums.ItemType;
import com.example.highloadsystemswardrobemanager.entity.enums.Season;
import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "wardrobe_items")
public class WardrobeItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 32)
    private ItemType type;

    @Column(name = "brand", length = 100)
    private String brand;

    @Column(name = "color", length = 40)
    private String color;

    @Enumerated(EnumType.STRING)
    @Column(name = "season", nullable = false, length = 16)
    private Season season;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist
    void onCreate() {
        if (createdAt == null) createdAt = Instant.now();
    }

    // getters/setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getOwner() { return owner; }
    public void setOwner(User owner) { this.owner = owner; }

    public ItemType getType() { return type; }
    public void setType(ItemType type) { this.type = type; }

    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public Season getSeason() { return season; }
    public void setSeason(Season season) { this.season = season; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
