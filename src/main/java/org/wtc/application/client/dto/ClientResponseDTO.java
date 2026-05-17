package org.wtc.application.client.dto;


import lombok.Builder;
import lombok.Data;
import java.util.Set;

@Data
@Builder
public class ClientResponseDTO {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String fcmToken;
    private Set<String> tagNames;
    private Set<String> segmentNames;
}
