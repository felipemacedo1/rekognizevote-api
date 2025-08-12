package com.rekognizevote.infrastructure.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Authentication tokens and user info")
public record AuthResponse(
    @Schema(description = "JWT access token")
    String accessToken,
    @Schema(description = "JWT refresh token")
    String refreshToken,
    @Schema(description = "Registered user data")
    UserResponse user
) {}