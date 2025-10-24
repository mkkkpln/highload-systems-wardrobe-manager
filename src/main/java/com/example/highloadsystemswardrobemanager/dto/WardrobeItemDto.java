package com.example.highloadsystemswardrobemanager.dto;

import com.example.highloadsystemswardrobemanager.entity.enums.ItemType;
import com.example.highloadsystemswardrobemanager.entity.enums.Season;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record WardrobeItemDto(
        Long id,

        @NotNull
        ItemType type,

        String brand,
        String color,

        @NotNull
        Season season,

        @NotBlank
        @Size(max = 500)
        String imageUrl,  // будет сериализован как image_url

        @NotNull
        Long ownerId  // будет сериализован как owner_id
) {
}
