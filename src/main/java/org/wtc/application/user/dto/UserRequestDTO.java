package org.wtc.application.user.dto;



import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRequestDTO {
    @NotBlank(message = "Name is required")
    private String fullName;


    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")

    private String role;
}