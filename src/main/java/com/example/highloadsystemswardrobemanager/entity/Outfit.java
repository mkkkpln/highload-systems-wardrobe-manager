package com.example.highloadsystemswardrobemanager.entity;

import com.example.highloadsystemswardrobemanager.entity.enums.OutfitRole;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "outfits")
public class Outfit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false, length = 120)
    private String title;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    // ✅ связь через OutfitItem
    @OneToMany(mappedBy = "outfit", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<OutfitItem> outfitItems = new HashSet<>();

    public Outfit() {}

    public Outfit(String title, User user) {
        this.title = title;
        this.user = user;
    }

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) createdAt = Instant.now();
    }

    // ✅ теперь используем OutfitRole
    public void addItem(WardrobeItem item, OutfitRole role, int positionIndex) {
        OutfitItem link = new OutfitItem(this, item, role, positionIndex);
        this.outfitItems.add(link);
    }

    public void clearItems() {
        this.outfitItems.clear();
    }

    // Getters/Setters
    public Long getId() { return id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Set<OutfitItem> getOutfitItems() { return outfitItems; }
    public void setOutfitItems(Set<OutfitItem> outfitItems) { this.outfitItems = outfitItems; }
}
