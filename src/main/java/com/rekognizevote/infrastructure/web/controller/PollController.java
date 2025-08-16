package com.rekognizevote.infrastructure.web.controller;

import com.rekognizevote.domain.port.PollRepository;
import com.rekognizevote.domain.valueobject.PollStatus;
import com.rekognizevote.infrastructure.web.dto.PollResponse;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/polls")
public class PollController {
    private final PollRepository pollRepository;

    public PollController(PollRepository pollRepository) {
        this.pollRepository = pollRepository;
    }

    @GetMapping
    public Flux<PollResponse> getPolls(@RequestParam(required = false) PollStatus status) {
        if (status != null) {
            return pollRepository.findByStatus(status)
                .map(poll -> PollResponse.from(poll, false)); // TODO: Check if user voted
        }
        return pollRepository.findAll()
            .map(poll -> PollResponse.from(poll, false)); // TODO: Check if user voted
    }

    @GetMapping("/{pollId}")
    public Mono<PollResponse> getPoll(@PathVariable String pollId) {
        return pollRepository.findById(pollId)
            .map(poll -> PollResponse.from(poll, false)); // TODO: Check if user voted
    }
}