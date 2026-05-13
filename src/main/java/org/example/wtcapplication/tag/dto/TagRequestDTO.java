package org.example.wtcapplication.tag.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class TagRequestDTO {

    @NotBlank(message = "Tag name is required")
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    private String name;

    @Pattern(regexp = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$",
            message = "Color must be a valid hex code (e.g., #FF5733)")
    private String color;

    @Size(max = 255, message = "Description cannot exceed 255 characters")
    private String description;
}
