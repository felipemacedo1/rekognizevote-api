package com.rekognizevote.domain.event;

import java.time.Instant;

public record UserRegisteredEvent(
    String userId,
    Instant occurredAt
) {
    public UserRegisteredEvent(String userId) {
        this(userId, Instant.now());
    }
}