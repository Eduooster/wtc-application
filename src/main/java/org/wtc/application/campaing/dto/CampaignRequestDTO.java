package org.wtc.application.campaing.dto;


import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CampaignRequestDTO {

    @NotBlank
    @Size(max = 100)
    private String title;

    @NotBlank
    private String content;

    @FutureOrPresent
    private LocalDateTime scheduledAt;

    @NotNull
    private Long targetSegmentId;

    @NotBlank
    private String internalRoute;
}