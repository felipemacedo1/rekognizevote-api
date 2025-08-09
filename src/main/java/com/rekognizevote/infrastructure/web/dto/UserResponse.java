package com.rekognizevote.infrastructure.web.dto;

import com.rekognizevote.domain.model.User;

import java.time.Instant;

public record UserResponse(
    String id,
    String name,
    String email,
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