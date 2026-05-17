package org.wtc.application.campaing.dto;


import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CampaignRequestDTO {

    @NotBlank(message = "Campaign title is required")
    @Size(max = 100, message = "Title cannot exceed 100 characters")
    private String title;

    @NotBlank(message = "Campaign content is required")
    private String content;

    @NotNull(message = "Campaign status is required")
    private String status;

    @FutureOrPresent(message = "Schedule date must be today or in the future")
    private LocalDateTime scheduledAt;

    @NotNull(message = "Creator ID is required")
    private Long creatorId;

    @NotNull(message = "Target segment ID is required")
    private Long targetSegmentId;
}