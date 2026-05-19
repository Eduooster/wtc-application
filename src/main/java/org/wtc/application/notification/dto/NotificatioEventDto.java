package org.wtc.application.notification.dto;

public record NotificatioEventDto(
        Long clientId,
        Long campaignId,
        String title,
        String body
) {
}
