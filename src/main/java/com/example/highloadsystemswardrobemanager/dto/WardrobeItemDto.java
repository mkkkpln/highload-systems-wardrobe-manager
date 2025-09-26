package com.example.highloadsystemswardrobemanager.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class WardrobeItemDto {
    private Long id;

    @NotBlank
    private String type;

    @NotBlank
    private String brand;

    @NotBlank
    private String color;

    @NotBlank
    private String season;

    @NotBlank
    @Size(max = 500)
    private String imageUrl;

    private Long ownerId;

    // 🔹 Геттеры/сеттеры
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public String getSeason() { return season; }
    public void setSeason(String season) { this.season = season; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public Long getOwnerId() { return ownerId; }
    public void setOwnerId(Long ownerId) { this.ownerId = ownerId; }
}
