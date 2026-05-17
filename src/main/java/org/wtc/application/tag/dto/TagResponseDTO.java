package org.wtc.application.tag.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TagResponseDTO {
    private Long id;
    private String name;
    private String color;
    private String description;
}
