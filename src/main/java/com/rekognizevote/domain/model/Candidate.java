package com.rekognizevote.domain.model;

import java.util.UUID;

public record Candidate(
    String id,
    String name,
    String description,
    String imageUrl,
    int voteCount
) {
    public Candidate {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Candidate name cannot be null or blank");
        }
    }

    public static Candidate create(String name, String description, String imageUrl) {
        return new Candidate(
            UUID.randomUUID().toString(),
            name,
            description,
            imageUrl,
            0
        );
    }

    public Candidate incrementVote() {
        return new Candidate(id, name, description, imageUrl, voteCount + 1);
    }

    public double calculatePercentage(int totalVotes) {
        return totalVotes == 0 ? 0.0 : (double) voteCount / totalVotes * 100.0;
    }
}