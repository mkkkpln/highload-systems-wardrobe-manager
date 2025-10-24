package com.example.highloadsystemswardrobemanager.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record OutfitDto(
        Long id,

        @NotBlank
        String title,

        Long userId,  // будет сериализован как user_id

        List<OutfitItemLinkDto> items
) {
}
