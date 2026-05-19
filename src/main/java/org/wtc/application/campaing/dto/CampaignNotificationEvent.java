package org.wtc.application.campaing.dto;

public record CampaignNotificationEvent(
        String token,
        String title,
        String content
) {}