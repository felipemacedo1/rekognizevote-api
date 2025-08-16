package com.rekognizevote.infrastructure.web.controller;

import com.rekognizevote.application.service.VotingService;
import com.rekognizevote.infrastructure.web.dto.VoteRequest;
import com.rekognizevote.infrastructure.web.dto.VoteResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/polls")
public class VoteController {
    private final VotingService votingService;

    public VoteController(VotingService votingService) {
        this.votingService = votingService;
    }

    @PostMapping("/{pollId}/vote")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<VoteResponse> vote(@PathVariable String pollId,
                                  @Valid @RequestBody VoteRequest request,
                                  @RequestHeader("Authorization") String authHeader) {
        // TODO: Extract userId from JWT token
        String userId = "mock-user-id"; // For MVP
        
        return votingService.submitVote(pollId, request.candidateId(), userId, request.faceImageUrl())
            .map(vote -> new VoteResponse(
                vote.getId(),
                vote.getPollId(),
                vote.getCandidateId(),
                vote.getUserId(),
                vote.getTimestamp(),
                vote.isVerified()
            ));
    }
}