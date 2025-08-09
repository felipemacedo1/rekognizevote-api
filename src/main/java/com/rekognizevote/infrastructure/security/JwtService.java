package com.rekognizevote.infrastructure.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;

@Service
public class JwtService {
    private final SecretKey secretKey;
    private final long accessTokenExpiration;
    private final long refreshTokenExpiration;

    public JwtService(@Value("${jwt.secret}") String secret,
                     @Value("${jwt.expiration}") long accessTokenExpiration,
                     @Value("${jwt.refresh-expiration}") long refreshTokenExpiration) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());
        this.accessTokenExpiration = accessTokenExpiration;
        this.refreshTokenExpiration = refreshTokenExpiration;
    }

    public Mono<String> generateAccessToken(String userId, String email) {
        return Mono.fromCallable(() -> 
            Jwts.builder()
                .subject(userId)
                .claim("email", email)
                .claim("type", "access")
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plusMillis(accessTokenExpiration)))
                .signWith(secretKey)
                .compact()
        );
    }

    public Mono<String> generateRefreshToken(String userId) {
        return Mono.fromCallable(() ->
            Jwts.builder()
                .subject(userId)
                .claim("type", "refresh")
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plusMillis(refreshTokenExpiration)))
                .signWith(secretKey)
                .compact()
        );
    }

    public Mono<Claims> validateToken(String token) {
        return Mono.fromCallable(() ->
            Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
        ).onErrorMap(Exception.class, ex -> new SecurityException("Invalid token"));
    }

    public Mono<String> extractUserId(String token) {
        return validateToken(token)
            .map(Claims::getSubject);
    }
}