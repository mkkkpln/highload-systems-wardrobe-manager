package com.example.highloadsystemswardrobemanager.entity;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class OutfitItemId implements Serializable {
    private Long outfitId;
    private Long itemId;

    public OutfitItemId() {}
    public OutfitItemId(Long outfitId, Long itemId) {
        this.outfitId = outfitId;
        this.itemId = itemId;
    }

    public Long getOutfitId() { return outfitId; }
    public void setOutfitId(Long outfitId) { this.outfitId = outfitId; }

    public Long getItemId() { return itemId; }
    public void setItemId(Long itemId) { this.itemId = itemId; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OutfitItemId)) return false;
        OutfitItemId that = (OutfitItemId) o;
        return Objects.equals(outfitId, that.outfitId) && Objects.equals(itemId, that.itemId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(outfitId, itemId);
    }
}
