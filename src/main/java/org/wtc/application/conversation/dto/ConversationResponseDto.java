package org.wtc.application.conversation.dto;

import org.wtc.application.conversation.enums.ConversationStatus;
import org.wtc.application.conversation.entity.Conversation;

import java.time.LocalDateTime;

public record ConversationResponseDto(

        Long id,
        String title,
        ConversationStatus status,
        LocalDateTime lastMessageAt,
        LocalDateTime createdAt

) {

    public ConversationResponseDto(Conversation conversation) {
        this(
                conversation.getId(),
                conversation.getTitle(),
                conversation.getStatus(),
                conversation.getLastMessageAt(),
                conversation.getCreatedAt()
        );
    }
}