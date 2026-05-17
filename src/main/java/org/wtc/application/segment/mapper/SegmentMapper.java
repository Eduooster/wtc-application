package org.wtc.application.segment.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.wtc.application.segment.dto.SegmentRequestDTO;
import org.wtc.application.segment.dto.SegmentResponseDTO;
import org.wtc.application.segment.entity.Segment;

@Mapper(componentModel = "spring")
public interface SegmentMapper {
    Segment toEntiy(SegmentRequestDTO request);
    SegmentResponseDTO toDto(Segment segment);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    void updateEntityFromDto(SegmentRequestDTO dto, @MappingTarget Segment entity);
}
