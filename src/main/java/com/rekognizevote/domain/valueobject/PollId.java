package com.rekognizevote.domain.valueobject;

import java.util.UUID;

public record PollId(String value) {
    public PollId {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("PollId cannot be null or blank");
        }
    }

    public static PollId generate() {
        return new PollId(UUID.randomUUID().toString());
    }

    public static PollId of(String value) {
        return new PollId(value);
    }
}