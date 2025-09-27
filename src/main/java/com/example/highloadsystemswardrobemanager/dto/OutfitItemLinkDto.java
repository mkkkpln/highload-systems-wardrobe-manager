package com.example.highloadsystemswardrobemanager.dto;

import com.example.highloadsystemswardrobemanager.entity.enums.OutfitRole;
import jakarta.validation.constraints.NotNull;

public class OutfitItemLinkDto {
    @NotNull
    private Long itemId;

    @NotNull
    private OutfitRole role;   // TOP/BOTTOM/SHOES/...

    public Long getItemId() { return itemId; }
    public void setItemId(Long itemId) { this.itemId = itemId; }

    public OutfitRole getRole() { return role; }
    public void setRole(OutfitRole role) { this.role = role; }
}
