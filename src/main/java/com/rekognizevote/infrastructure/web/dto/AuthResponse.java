package com.rekognizevote.infrastructure.web.dto;

public record AuthResponse(
    String accessToken,
    String refreshToken,
    UserResponse user
) {}