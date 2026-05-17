package org.wtc.application.tag.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.wtc.application.tag.dto.TagRequestDTO;
import org.wtc.application.tag.dto.TagResponseDTO;
import org.wtc.application.tag.entity.Tag;

@Mapper(componentModel = "spring")
public interface TagMapper {
    TagResponseDTO toDTO(Tag entity);
    Tag toEntity(TagRequestDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    void updateEntityFromDto(TagRequestDTO dto, @MappingTarget Tag entity);
}
