package com.rekognizevote.domain.port;

import com.rekognizevote.domain.model.Poll;
import com.rekognizevote.domain.valueobject.PollId;
import com.rekognizevote.domain.valueobject.PollStatus;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PollRepository {
    Mono<Poll> save(Poll poll);
    Mono<Poll> findById(PollId id);
    Flux<Poll> findByStatus(PollStatus status);
    Flux<Poll> findAll();
    Mono<Void> delete(PollId id);
}