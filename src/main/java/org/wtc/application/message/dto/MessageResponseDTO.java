package org.wtc.application.message.dto;

import org.wtc.application.message.entitity.Message;
import org.wtc.application.message.enums.MessageStatus;

import org.wtc.application.message.enums.ParticipantType;

import java.time.LocalDateTime;

public record MessageResponseDTO(

        Long id,
        String content,
        MessageStatus status,

        ParticipantType senderType,
        Long senderId,

        ParticipantType receiverType,
        Long receiverId,

        Boolean read,
        LocalDateTime sentAt,
        Long conversationId

) {

    public MessageResponseDTO(Message message) {
        this(
                message.getId(),
                message.getContent(),
                message.getStatus(),

                message.getSender().getParticipantType(),
                message.getSender().getRefId(),

                message.getReceiver().getParticipantType(),
                message.getReceiver().getRefId(),

                message.getRead(),
                message.getSentAt(),
                message.getConversation().getId()
        );
    }
}