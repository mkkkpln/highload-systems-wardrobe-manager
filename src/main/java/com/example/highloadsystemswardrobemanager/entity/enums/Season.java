package com.example.highloadsystemswardrobemanager.entity.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Season {
    WINTER, SPRING, SUMMER, AUTUMN, ALL_SEASONS;

    @JsonCreator
    public static Season fromString(String value) {
        return switch (value.toUpperCase().replace("-", "_")) {
            case "ALLSEASON", "ALL_SEASON", "ALLSEASONS" -> ALL_SEASONS;
            default -> Season.valueOf(value.toUpperCase());
        };
    }
}
