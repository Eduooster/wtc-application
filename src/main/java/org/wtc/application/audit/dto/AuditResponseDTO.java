package org.wtc.application.audit.dto;


import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class AuditResponseDTO {
    private Long id;
    private String action;
    private String details;
    private LocalDateTime timestamp;
    private Long authorId;
    private String authorName;
    private Long entityId;
    private String entityName;
}