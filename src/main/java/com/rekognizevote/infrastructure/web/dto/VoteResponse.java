package com.rekognizevote.infrastructure.web.dto;

import java.time.Instant;

public record VoteResponse(
    String id,
    String pollId,
    String candidateId,
    String userId,
    Instant timestamp,
    boolean verified
) {}