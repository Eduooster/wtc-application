package org.wtc.application.notification.dto;

import java.time.LocalDateTime;

public record NotificationResponseDTO(
        Long id,
        Long conversationId,
        Long receiverId,
        String previewContent,
        boolean read,
        LocalDateTime createdAt
) {}
