package com.example.highloadsystemswardrobemanager.entity;

import com.example.highloadsystemswardrobemanager.entity.enums.OutfitRole;
import jakarta.persistence.*;

@Entity
@Table(name = "outfit_items")
public class OutfitItem {

    @EmbeddedId
    private OutfitItemId id = new OutfitItemId();

    @ManyToOne
    @MapsId("outfitId") // связывает с PK
    @JoinColumn(name = "outfit_id", nullable = false)
    private Outfit outfit;

    @ManyToOne
    @MapsId("itemId") // связывает с PK
    @JoinColumn(name = "item_id", nullable = false)
    private WardrobeItem item;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", length = 50, nullable = false)
    private OutfitRole role;

    @Column(name = "position_index", nullable = false)
    private int positionIndex;

    public OutfitItem() {}

    public OutfitItem(Outfit outfit, WardrobeItem item, OutfitRole role, int positionIndex) {
        this.outfit = outfit;
        this.item = item;
        this.role = role;
        this.positionIndex = positionIndex;
        this.id = new OutfitItemId(outfit.getId(), item.getId());
    }

    public OutfitItemId getId() { return id; }
    public void setId(OutfitItemId id) { this.id = id; }

    public Outfit getOutfit() { return outfit; }
    public void setOutfit(Outfit outfit) { this.outfit = outfit; }

    public WardrobeItem getItem() { return item; }
    public void setItem(WardrobeItem item) { this.item = item; }

    public OutfitRole getRole() { return role; }
    public void setRole(OutfitRole role) { this.role = role; }

    public int getPositionIndex() { return positionIndex; }
    public void setPositionIndex(int positionIndex) { this.positionIndex = positionIndex; }
}
