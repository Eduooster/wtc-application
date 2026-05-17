package org.wtc.application.conversation.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserConversationRequestDTO(
        @NotBlank String title,
        @NotBlank String firstMessage,
        @NotNull Long clientId){}
