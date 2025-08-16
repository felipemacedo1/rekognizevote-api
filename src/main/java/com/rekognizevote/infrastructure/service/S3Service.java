package com.rekognizevote.infrastructure.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Service
public class S3Service {
    private final S3Presigner s3Presigner;
    
    @Value("${aws.s3.bucket}")
    private String bucketName;

    public S3Service(S3Presigner s3Presigner) {
        this.s3Presigner = s3Presigner;
    }

    public Mono<PresignedUrlResponse> generatePresignedUrl(String userId, String fileType) {
        return Mono.fromCallable(() -> {
            String timestamp = String.valueOf(Instant.now().getEpochSecond());
            String fileKey = String.format("face_evidence/%s/%s_%s.jpg", userId, timestamp, UUID.randomUUID());
            
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fileKey)
                .contentType("image/jpeg")
                .build();

            PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(15))
                .putObjectRequest(putObjectRequest)
                .build();

            PresignedPutObjectRequest presignedRequest = s3Presigner.presignPutObject(presignRequest);
            
            return new PresignedUrlResponse(
                presignedRequest.url().toString(),
                fileKey,
                900 // 15 minutes
            );
        });
    }

    public record PresignedUrlResponse(
        String uploadUrl,
        String fileKey,
        int expiresIn
    ) {}
}