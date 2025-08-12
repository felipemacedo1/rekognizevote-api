package com.rekognizevote.infrastructure.web.dto;

import com.rekognizevote.domain.model.User;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

@Schema(description = "User information")
public record UserResponse(
    @Schema(description = "User identifier", example = "42f41e48-9836-4aa4-8bf9-daf9b0fa8229")
    String id,
    @Schema(description = "User name", example = "Alice Doe")
    String name,
    @Schema(description = "Email address", example = "alice@example.com")
    String email,
    @Schema(description = "Creation timestamp", example = "2024-05-13T13:51:08.123Z")
    Instant createdAt
) {
    public static UserResponse from(User user) {
        return new UserResponse(
            user.getId(),
            user.getName(),
            user.getEmail(),
            user.getCreatedAt()
        );
    }
}