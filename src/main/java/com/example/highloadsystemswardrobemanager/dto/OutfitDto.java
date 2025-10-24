package com.example.highloadsystemswardrobemanager.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record OutfitDto(
        Long id,

        @NotBlank
        String title,

        @NotNull
        Long userId,

        @Valid
        List<OutfitItemLinkDto> items
) {
}
