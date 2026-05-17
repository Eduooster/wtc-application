package org.wtc.application.message;

import org.mapstruct.Mapper;
import org.wtc.application.message.dto.MessageResponseDTO;
import org.wtc.application.message.entitity.Message;

@Mapper(componentModel = "spring")
public interface MessageMapper {
    MessageResponseDTO toResponseDto(Message message);
}
