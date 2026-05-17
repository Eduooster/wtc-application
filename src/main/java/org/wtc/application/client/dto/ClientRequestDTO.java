package org.wtc.application.client.dto;



import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

@Data
public class ClientRequestDTO {

    @NotBlank(message = "Client name is required")
    @Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters")
    private String name;

    private String phone;

    private String fcmToken;

    private Set<Long> tagIds;

    private Set<Long> segmentIds;
}
