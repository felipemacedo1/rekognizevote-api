package com.rekognizevote.application.service;

import com.rekognizevote.domain.model.Vote;
import com.rekognizevote.domain.port.PollRepository;
import com.rekognizevote.domain.port.VoteRepository;
import com.rekognizevote.domain.valueobject.PollId;
import com.rekognizevote.domain.valueobject.UserId;
import com.rekognizevote.infrastructure.service.FaceVerificationService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@Service
public class VotingService {
    private final VoteRepository voteRepository;
    private final PollRepository pollRepository;
    private final FaceVerificationService faceVerificationService;

    public VotingService(VoteRepository voteRepository, 
                        PollRepository pollRepository,
                        FaceVerificationService faceVerificationService) {
        this.voteRepository = voteRepository;
        this.pollRepository = pollRepository;
        this.faceVerificationService = faceVerificationService;
    }

    public Mono<Vote> submitVote(String pollId, String candidateId, String userId, String faceImageUrl) {
        PollId pollIdObj = PollId.of(pollId);
        UserId userIdObj = UserId.of(userId);

        return validateVoteEligibility(pollIdObj, userIdObj, candidateId)
            .then(faceVerificationService.verifyFace(userId, faceImageUrl))
            .flatMap(verified -> {
                if (!verified) {
                    return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Face verification failed"));
                }
                
                Vote vote = Vote.builder()
                    .pollId(pollIdObj)
                    .candidateId(candidateId)
                    .userId(userIdObj)
                    .faceImageKey(extractImageKey(faceImageUrl))
                    .verified(true)
                    .build();
                
                return voteRepository.save(vote);
            });
    }

    private Mono<Void> validateVoteEligibility(PollId pollId, UserId userId, String candidateId) {
        return pollRepository.findById(pollId.value())
            .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Poll not found")))
            .flatMap(poll -> {
                if (!poll.canUserVote()) {
                    return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Poll not active"));
                }
                
                boolean candidateExists = poll.getCandidates().stream()
                    .anyMatch(candidate -> candidate.getId().equals(candidateId));
                
                if (!candidateExists) {
                    return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Candidate not found"));
                }
                
                return voteRepository.existsByPollIdAndUserId(pollId, userId)
                    .flatMap(hasVoted -> {
                        if (hasVoted) {
                            return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "User already voted"));
                        }
                        return Mono.empty();
                    });
            });
    }

    private String extractImageKey(String faceImageUrl) {
        // Extract S3 key from URL
        // Example: https://bucket.s3.amazonaws.com/face_evidence/user_id/timestamp.jpg
        // Returns: face_evidence/user_id/timestamp.jpg
        if (faceImageUrl.contains("/")) {
            String[] parts = faceImageUrl.split("/");
            if (parts.length >= 3) {
                return String.join("/", java.util.Arrays.copyOfRange(parts, parts.length - 3, parts.length));
            }
        }
        return faceImageUrl;
    }
}