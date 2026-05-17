package org.wtc.application.campaing.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.wtc.application.campaing.dto.CampaignRequestDTO;
import org.wtc.application.campaing.dto.CampaignResponseDTO;
import org.wtc.application.campaing.entity.Campaign;

@Mapper(componentModel = "spring")
public interface CampaignMapper {

    Campaign toEntiy(CampaignRequestDTO campaign);
    CampaignResponseDTO toDTO(Campaign campaign);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    void updateEntityFromDto(CampaignRequestDTO dto, @MappingTarget Campaign entity);
}
