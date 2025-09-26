package com.example.highloadsystemswardrobemanager.mapper;

import com.example.highloadsystemswardrobemanager.dto.UserDto;
import com.example.highloadsystemswardrobemanager.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toDto(User entity);

    // Обычно id генерит БД, поэтому при create можно игнорировать id из dto,
    // но для update можно маппить. Оставим общий случай.
    @Mapping(target = "items", ignore = true)
    @Mapping(target = "outfits", ignore = true)
    User toEntity(UserDto dto);
}
