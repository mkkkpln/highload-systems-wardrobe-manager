package com.example.highloadsystemswardrobemanager.dto;

import com.example.highloadsystemswardrobemanager.entity.enums.ItemType;
import com.example.highloadsystemswardrobemanager.entity.enums.Season;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class WardrobeItemDto {
    private Long id;

    @NotNull
    private ItemType type;

    private String brand;
    private String color;

    @NotNull
    private Season season;

    @NotBlank
    @Size(max = 500)
    private String imageUrl;

    @NotNull
    private Long ownerId;

    // 🔹 Геттеры/сеттеры
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

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

    public Long getOwnerId() { return ownerId; }
    public void setOwnerId(Long ownerId) { this.ownerId = ownerId; }
}
