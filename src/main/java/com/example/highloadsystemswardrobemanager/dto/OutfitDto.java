package com.example.highloadsystemswardrobemanager.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.List;

public class OutfitDto {
    private Long id;

    @NotBlank
    private String title;

    private Long userId;

    private List<OutfitItemLinkDto> items; // <— тут

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public List<OutfitItemLinkDto> getItems() { return items; }
    public void setItems(List<OutfitItemLinkDto> items) { this.items = items; }
}
