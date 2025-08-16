package com.rekognizevote.infrastructure.web.dto;

import jakarta.validation.constraints.NotBlank;

public record VoteRequest(
    @NotBlank(message = "Candidate ID is required")
    String candidateId,
    
    @NotBlank(message = "Face image URL is required")
    String faceImageUrl
) {}