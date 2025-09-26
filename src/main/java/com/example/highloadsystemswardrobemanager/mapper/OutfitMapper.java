package com.example.highloadsystemswardrobemanager.mapper;

import com.example.highloadsystemswardrobemanager.dto.OutfitDto;
import com.example.highloadsystemswardrobemanager.entity.Outfit;
import com.example.highloadsystemswardrobemanager.entity.WardrobeItem;
import org.mapstruct.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface OutfitMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(target = "itemIds", expression = "java(toItemIds(outfit.getItems()))")
    OutfitDto toDto(Outfit outfit);

    // dto -> entity: только простые поля; user и items проставим в сервисе
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "items", ignore = true)
    @Mapping(target = "createdAt", ignore = true) // задаётся @PrePersist или в БД
    Outfit toEntity(OutfitDto dto);

    // helper
    default List<Long> toItemIds(Set<WardrobeItem> items) {
        return items == null ? List.of()
                : items.stream().map(WardrobeItem::getId).collect(Collectors.toList());
    }
}
