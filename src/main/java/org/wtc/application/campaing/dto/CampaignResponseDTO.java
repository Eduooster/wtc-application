package org.wtc.application.campaing.dto;



import lombok.Builder;
import lombok.Data;
import org.wtc.application.campaing.entity.Campaign;

import java.time.LocalDateTime;

public record CampaignResponseDTO(
        Long id,
        String title,
        String content,
        String status,
        LocalDateTime scheduledAt,
        LocalDateTime createdAt,
        Long creator,
        String targetSegment
) {

    public CampaignResponseDTO(Campaign campaign) {
        this(
                campaign.getId(),
                campaign.getTitle(),
                campaign.getContent(),
                campaign.getStatus() != null ? campaign.getStatus().name() : null,
                campaign.getScheduledAt(),
                campaign.getCreatedAt(),
                campaign.getCreator() != null ? campaign.getCreator().getId() : null,
                campaign.getTargetSegment() != null ? campaign.getTargetSegment().getName() : null
        );
    }
}