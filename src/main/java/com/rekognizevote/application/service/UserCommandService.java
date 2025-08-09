package com.rekognizevote.application.service;

import com.rekognizevote.application.command.RegisterUserCommand;
import com.rekognizevote.domain.event.UserRegisteredEvent;
import com.rekognizevote.domain.model.User;
import com.rekognizevote.domain.port.UserRepository;
import com.rekognizevote.domain.valueobject.Email;
import com.rekognizevote.domain.valueobject.UserId;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@Transactional
public class UserCommandService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher eventPublisher;

    public UserCommandService(UserRepository userRepository, 
                             PasswordEncoder passwordEncoder,
                             ApplicationEventPublisher eventPublisher) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.eventPublisher = eventPublisher;
    }

    public Mono<User> registerUser(RegisterUserCommand command) {
        Email email = Email.of(command.email());
        
        return userRepository.existsByEmail(email)
            .flatMap(exists -> {
                if (exists) {
                    return Mono.error(new IllegalArgumentException("Email already exists"));
                }
                
                User user = User.builder()
                    .id(UserId.generate())
                    .name(command.name())
                    .email(email)
                    .passwordHash(passwordEncoder.encode(command.password()))
                    .build();
                
                return userRepository.save(user)
                    .doOnSuccess(savedUser -> 
                        eventPublisher.publishEvent(new UserRegisteredEvent(savedUser.getId())));
            });
    }
}