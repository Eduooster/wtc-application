package org.wtc.application.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ClientRegistrationDto(
        @NotBlank @Email String email,
        @NotBlank String fullName,
        @NotBlank String password,
        @NotBlank String companyName,
        @NotBlank String taxId,
        @NotBlank String phoneNumber
) {}