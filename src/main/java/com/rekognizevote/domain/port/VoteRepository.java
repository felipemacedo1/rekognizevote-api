package com.rekognizevote.domain.port;

import com.rekognizevote.domain.model.Vote;
import com.rekognizevote.domain.valueobject.PollId;
import com.rekognizevote.domain.valueobject.UserId;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface VoteRepository {
    Mono<Vote> save(Vote vote);
    Mono<Vote> findById(String id);
    Flux<Vote> findByPollId(PollId pollId);
    Mono<Boolean> existsByPollIdAndUserId(PollId pollId, UserId userId);
    Mono<Long> countByPollIdAndCandidateId(PollId pollId, String candidateId);
}