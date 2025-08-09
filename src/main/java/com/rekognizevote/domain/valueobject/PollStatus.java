package com.rekognizevote.domain.valueobject;

public enum PollStatus {
    UPCOMING, ACTIVE, CLOSED;

    public boolean isActive() {
        return this == ACTIVE;
    }

    public boolean canVote() {
        return this == ACTIVE;
    }
}