package com.rekognizevote.infrastructure.web.dto;

import com.rekognizevote.domain.model.Candidate;

public record CandidateResponse(
    String id,
    String name,
    String description,
    String imageUrl,
    int voteCount,
    double percentage
) {
    public static CandidateResponse from(Candidate candidate) {
        return new CandidateResponse(
            candidate.getId(),
            candidate.getName(),
            candidate.getDescription(),
            candidate.getImageUrl(),
            candidate.getVoteCount(),
            candidate.getPercentage()
        );
    }
}