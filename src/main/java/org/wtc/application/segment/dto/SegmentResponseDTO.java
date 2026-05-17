package org.wtc.application.segment.dto;



import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class SegmentResponseDTO {
    private Long id;
    private String name;
    private String description;
    private LocalDateTime createdAt;
}