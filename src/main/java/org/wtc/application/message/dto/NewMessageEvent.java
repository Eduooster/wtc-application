package org.wtc.application.message.dto;


import org.wtc.application.message.enums.ParticipantType;

public record NewMessageEvent(
        Long messageId,
        Long conversationId,
        Long senderRefId,
        ParticipantType senderType,
        Long receiverRefId,
        ParticipantType receiverType,
        String content
) {}