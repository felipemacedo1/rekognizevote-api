package com.rekognizevote.domain.port;

import com.rekognizevote.domain.model.User;
import com.rekognizevote.domain.valueobject.Email;
import com.rekognizevote.domain.valueobject.UserId;
import reactor.core.publisher.Mono;

public interface UserRepository {
    Mono<User> save(User user);
    Mono<User> findById(UserId id);
    Mono<User> findByEmail(Email email);
    Mono<Boolean> existsByEmail(Email email);
    Mono<Void> delete(UserId id);
}