package org.wtc.application.conversation.dto;

import jakarta.validation.constraints.NotBlank;

public record ClientConversationRequestDTO(
        @NotBlank String title,
        @NotBlank String firstMessage
) {}
