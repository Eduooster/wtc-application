package org.wtc.application.audit.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AuditRequestDTO {

    @NotBlank(message = "Action is required")
    @Size(max = 50, message = "Action name must not exceed 50 characters")
    private String action;

    @NotBlank(message = "Details are required")
    private String details;

    @NotNull(message = "Author ID is required")
    private Long authorId;

    @NotNull(message = "Entity ID is required")
    private Long entityId;

    @NotBlank(message = "Entity name is required")
    private String entityName;
}
