package com.example.highloadsystemswardrobemanager.entity.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum ItemType {
    T_SHIRT, SHIRT, SWEATER, JACKET, COAT,
    PANTS, JEANS, SKIRT, DRESS,
    SHOES, ACCESSORY;

    @JsonCreator
    public static ItemType fromString(String value) {
        return switch (value.toUpperCase().replace("-", "_")) {
            case "T_SHIRT" -> T_SHIRT;
            case "PANTS" -> PANTS;
            case "SHIRT" -> SHIRT;
            default -> ItemType.valueOf(value.toUpperCase());
        };
    }
}
