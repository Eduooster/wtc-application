package org.wtc.application.conversation.mapper;

import org.mapstruct.Mapper;
import org.wtc.application.conversation.dto.ConversationResponseDto;
import org.wtc.application.conversation.entity.Conversation;


@Mapper(componentModel = "spring")
public interface ConversationMapper {



    ConversationResponseDto toResponseDto(Conversation conversation);
}
