package org.wtc.application.client.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.wtc.application.campaing.dto.CampaignResponseDTO;
import org.wtc.application.client.dto.ClientRequestDTO;
import org.wtc.application.client.dto.ClientResponseDTO;
import org.wtc.application.client.entity.Client;
import org.wtc.application.user.dto.UserRequestDTO;
import org.wtc.application.user.dto.UserResponseDTO;
import org.wtc.application.user.entity.User;

@Mapper(componentModel = "spring")
public interface ClientMapper {
     Client toEntity(ClientRequestDTO clientRequestDTO);
     ClientResponseDTO toDTO(Client client);

     @Mapping(target = "id", ignore = true)
     @Mapping(target = "createdAt", ignore = true)
     @Mapping(target = "updatedAt", ignore = true)
     @Mapping(target = "deleted", ignore = true)
     void updateEntityFromDto(ClientRequestDTO dto, @MappingTarget Client entity);
}
