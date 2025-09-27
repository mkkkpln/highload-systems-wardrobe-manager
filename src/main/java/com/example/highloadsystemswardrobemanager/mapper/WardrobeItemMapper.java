package com.example.highloadsystemswardrobemanager.mapper;

import com.example.highloadsystemswardrobemanager.dto.WardrobeItemDto;
import com.example.highloadsystemswardrobemanager.entity.WardrobeItem;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface WardrobeItemMapper {

    // entity -> dto: owner.id -> ownerId
    @Mapping(source = "owner.id", target = "ownerId")
    WardrobeItemDto toDto(WardrobeItem entity);

    // dto -> entity: owner НЕ маппим (ставим в сервисе)
    @Mapping(target = "owner", ignore = true)
    @Mapping(target = "createdAt", ignore = true) // пусть БД или @PrePersist проставит
    WardrobeItem toEntity(WardrobeItemDto dto);

    // обновление существующей entity из dto
    @Mapping(target = "owner", ignore = true)     // owner не трогаем
    @Mapping(target = "createdAt", ignore = true) // createdAt не меняем
    void updateEntityFromDto(WardrobeItemDto dto, @MappingTarget WardrobeItem entity);
}
