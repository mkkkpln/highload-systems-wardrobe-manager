package com.example.highloadsystemswardrobemanager.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record UserDto(
        Long id,

        @Email
        @NotBlank
        String email,

        @NotBlank
        @Size(min = 2, max = 100)
        String name
) {
}
