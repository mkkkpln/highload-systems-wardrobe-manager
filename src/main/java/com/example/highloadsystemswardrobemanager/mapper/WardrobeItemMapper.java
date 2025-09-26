package com.example.highloadsystemswardrobemanager.mapper;

import com.example.highloadsystemswardrobemanager.dto.WardrobeItemDto;
import com.example.highloadsystemswardrobemanager.entity.WardrobeItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface WardrobeItemMapper {

    // owner.id -> ownerId
    @Mapping(source = "owner.id", target = "ownerId")
    WardrobeItemDto toDto(WardrobeItem entity);

    // Обратное маппирование ownerId -> owner НЕ делаем в маппере,
    // потому что нужно грузить User из БД и кидать 404 — это задача сервиса.
}
