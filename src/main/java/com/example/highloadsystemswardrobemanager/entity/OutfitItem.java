package com.example.highloadsystemswardrobemanager.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "outfit_items")
public class OutfitItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 🔗 связь с Outfit
    @ManyToOne
    @JoinColumn(name = "outfit_id", nullable = false)
    private Outfit outfit;

    // 🔗 связь с WardrobeItem
    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private WardrobeItem item;

    // Дополнительное поле (например, роль вещи в образе)
    @Column(name = "role", length = 50)
    private String role;

    public OutfitItem() {}

    public OutfitItem(Outfit outfit, WardrobeItem item, String role) {
        this.outfit = outfit;
        this.item = item;
        this.role = role;
    }

    // getters/setters
    public Long getId() { return id; }

    public Outfit getOutfit() { return outfit; }
    public void setOutfit(Outfit outfit) { this.outfit = outfit; }

    public WardrobeItem getItem() { return item; }
    public void setItem(WardrobeItem item) { this.item = item; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}
