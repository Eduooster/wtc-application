package org.wtc.application.message.dto;

import jakarta.validation.constraints.NotBlank;
import org.wtc.application.message.enums.ParticipantType;

public record SendMessageRequestDTO (
        @NotBlank
        String content
        //FUTURAMENTE ADD MESSAGEtype

) {

}
