package com.example.highloadsystemswardrobemanager.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;

public class OutfitItemLinkDto {
    @NotNull
    private Long itemId;

    @NotBlank
    private String role; // например: "TOP", "BOTTOM", "SHOES"

    public Long getItemId() { return itemId; }
    public void setItemId(Long itemId) { this.itemId = itemId; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}
