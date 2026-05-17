package org.wtc.application.auth.dto;

public record TokenResponseDto(
        String accessToken,
        String refreshToken
) {}
