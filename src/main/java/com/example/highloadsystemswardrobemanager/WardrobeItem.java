package com.example.highloadsystemswardrobemanager;

import jakarta.persistence.Entity;
import jakarta.persistence.*;

@Entity
@Table(name = "wardrobe_items")
public class WardrobeItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    private String type;
    private String brand;
    private String color;
    private String season;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @Column(name = "created_at", nullable = false)
    private java.time.Instant createdAt = java.time.Instant.now();

    // 🔹 Геттеры
    public Long getId() { return id; }

    public User getOwner() { return owner; }
    public String getType() { return type; }
    public String getBrand() { return brand; }
    public String getColor() { return color; }
    public String getSeason() { return season; }
    public String getImageUrl() { return imageUrl; }
    public java.time.Instant getCreatedAt() { return createdAt; }

    // 🔹 Сеттеры
    public void setId(Long id) { this.id = id; }

    public void setOwner(User owner) { this.owner = owner; }
    public void setType(String type) { this.type = type; }
    public void setBrand(String brand) { this.brand = brand; }
    public void setColor(String color) { this.color = color; }
    public void setSeason(String season) { this.season = season; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public void setCreatedAt(java.time.Instant createdAt) { this.createdAt = createdAt; }
}
