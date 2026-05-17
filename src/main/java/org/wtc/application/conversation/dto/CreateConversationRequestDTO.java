package org.wtc.application.conversation.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateConversationRequestDTO(

        @NotBlank
        String title,

        @NotBlank
        String firstMessage,

        Long clientId



) {
}
