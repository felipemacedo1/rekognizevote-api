package com.rekognizevote.infrastructure.web.controller;

import com.rekognizevote.application.command.RegisterUserCommand;
import com.rekognizevote.application.service.UserCommandService;
import com.rekognizevote.infrastructure.security.JwtService;
import com.rekognizevote.infrastructure.web.dto.AuthResponse;
import com.rekognizevote.infrastructure.web.dto.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication")
public class AuthController {
    private final UserCommandService userCommandService;
    private final JwtService jwtService;

    public AuthController(UserCommandService userCommandService, JwtService jwtService) {
        this.userCommandService = userCommandService;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Register a new user", responses = {
        @ApiResponse(responseCode = "201", description = "User registered", content = @Content(schema = @Schema(implementation = AuthResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
        @ApiResponse(responseCode = "500", description = "Server error", content = @Content)
    })
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