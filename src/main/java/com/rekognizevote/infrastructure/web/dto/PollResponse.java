package com.rekognizevote.infrastructure.web.dto;

import com.rekognizevote.domain.model.Poll;
import com.rekognizevote.domain.valueobject.PollStatus;

import java.time.Instant;
import java.util.List;

public record PollResponse(
    String id,
    String title,
    String description,
    PollStatus status,
    boolean isPrivate,
    String accessCode,
    Instant startDate,
    Instant endDate,
    List<CandidateResponse> candidates,
    int totalVotes,
    boolean hasUserVoted,
    Instant createdAt
) {
    public static PollResponse from(Poll poll, boolean hasUserVoted) {
        return new PollResponse(
            poll.getId().value(),
            poll.getTitle(),
            poll.getDescription(),
            poll.getStatus(),
            poll.isPrivate(),
            poll.getAccessCode(),
            poll.getStartDate(),
            poll.getEndDate(),
            poll.getCandidates().stream()
                .map(CandidateResponse::from)
                .toList(),
            poll.getTotalVotes(),
            hasUserVoted,
            poll.getCreatedAt()
        );
    }
}