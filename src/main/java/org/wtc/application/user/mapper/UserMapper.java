package org.wtc.application.user.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.wtc.application.user.dto.UserRequestDTO;
import org.wtc.application.user.dto.UserResponseDTO;
import org.wtc.application.user.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponseDTO toDTO(User entity);
    User toEntity(UserRequestDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    void updateEntityFromDto(UserRequestDTO dto, @MappingTarget User entity);
}
