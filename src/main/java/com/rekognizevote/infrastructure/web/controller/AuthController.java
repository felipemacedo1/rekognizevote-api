package com.rekognizevote.infrastructure.web.controller;

import com.rekognizevote.application.command.RegisterUserCommand;
import com.rekognizevote.application.service.UserCommandService;
import com.rekognizevote.infrastructure.web.dto.AuthResponse;
import com.rekognizevote.infrastructure.web.dto.UserResponse;
import com.rekognizevote.infrastructure.security.JwtService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserCommandService userCommandService;
    private final JwtService jwtService;

    public AuthController(UserCommandService userCommandService, JwtService jwtService) {
        this.userCommandService = userCommandService;
        this.jwtService = jwtService;
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
}