package com.rekognizevote.infrastructure.web.controller;

import com.rekognizevote.infrastructure.service.S3Service;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/upload")
public class UploadController {
    private final S3Service s3Service;

    public UploadController(S3Service s3Service) {
        this.s3Service = s3Service;
    }

    @GetMapping("/presigned-url")
    public Mono<S3Service.PresignedUrlResponse> getPresignedUrl(
            @RequestParam String type,
            @RequestHeader("Authorization") String authHeader) {
        
        // TODO: Extract userId from JWT token
        String userId = "mock-user-id"; // For MVP
        
        return s3Service.generatePresignedUrl(userId, type);
    }
}