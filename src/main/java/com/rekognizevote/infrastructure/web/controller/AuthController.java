package com.rekognizevote.infrastructure.web.controller;

import com.rekognizevote.application.command.RegisterUserCommand;
import com.rekognizevote.application.service.UserCommandService;
import com.rekognizevote.domain.port.UserRepository;
import com.rekognizevote.infrastructure.web.dto.*;
import com.rekognizevote.infrastructure.security.JwtService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserCommandService userCommandService;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserCommandService userCommandService, 
                         UserRepository userRepository,
                         JwtService jwtService,
                         PasswordEncoder passwordEncoder) {
        this.userCommandService = userCommandService;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<AuthResponse> register(@Valid @RequestBody RegisterUserCommand command) {
        return userCommandService.registerUser(command)
            .flatMap(user -> 
                Mono.zip(
                    jwtService.generateAccessToken(user.getId(), user.getEmail()),
                    jwtService.generateRefreshToken(user.getId())
                ).map(tokens -> new AuthResponse(
                    tokens.getT1(),
                    tokens.getT2(),
                    UserResponse.from(user)
                ))
            );
    }

    @PostMapping("/login")
    public Mono<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return userRepository.findByEmail(request.email())
            .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials")))
            .filter(user -> passwordEncoder.matches(request.password(), user.getPasswordHash()))
            .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials")))
            .flatMap(user -> 
                Mono.zip(
                    jwtService.generateAccessToken(user.getId(), user.getEmail()),
                    jwtService.generateRefreshToken(user.getId())
                ).map(tokens -> new AuthResponse(
                    tokens.getT1(),
                    tokens.getT2(),
                    UserResponse.from(user)
                ))
            );
    }

    @PostMapping("/refresh")
    public Mono<AuthResponse> refresh(@Valid @RequestBody RefreshTokenRequest request) {
        return jwtService.validateRefreshToken(request.refreshToken())
            .flatMap(userId -> userRepository.findById(userId))
            .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid refresh token")))
            .flatMap(user -> 
                Mono.zip(
                    jwtService.generateAccessToken(user.getId(), user.getEmail()),
                    jwtService.generateRefreshToken(user.getId())
                ).map(tokens -> new AuthResponse(
                    tokens.getT1(),
                    tokens.getT2(),
                    UserResponse.from(user)
                ))
            );
    }

    @GetMapping("/me")
    public Mono<UserResponse> getCurrentUser(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        return jwtService.validateAccessToken(token)
            .flatMap(userId -> userRepository.findById(userId))
            .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token")))
            .map(UserResponse::from);
    }
}