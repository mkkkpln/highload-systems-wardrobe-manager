package com.example.highloadsystemswardrobemanager.mapper;

import com.example.highloadsystemswardrobemanager.dto.OutfitDto;
import com.example.highloadsystemswardrobemanager.dto.OutfitItemLinkDto;
import com.example.highloadsystemswardrobemanager.entity.Outfit;
import com.example.highloadsystemswardrobemanager.entity.OutfitItem;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OutfitMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(target = "items", expression = "java(toLinks(outfit))")
    OutfitDto toDto(Outfit outfit);

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "outfitItems", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Outfit toEntity(OutfitDto dto);

    // helper
    default List<OutfitItemLinkDto> toLinks(Outfit outfit) {
        return outfit.getOutfitItems().stream().map(oi -> {
            OutfitItemLinkDto d = new OutfitItemLinkDto();
            d.setItemId(oi.getItem().getId());
            d.setRole(oi.getRole());
            return d;
        }).toList();
    }
}
