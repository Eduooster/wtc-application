package org.wtc.application.campaing.dto;



import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class CampaignResponseDTO {
    private Long id;
    private String title;
    private String content;
    private String status;
    private LocalDateTime scheduledAt;
    private LocalDateTime createdAt;
    private Long creatorId;
    private String targetSegmentName;
}
