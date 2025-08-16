package com.rekognizevote.domain.model;

import com.rekognizevote.domain.valueobject.PollId;
import com.rekognizevote.domain.valueobject.UserId;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@DynamoDbBean
public class Vote {
    private String id;
    private PollId pollId;
    private String candidateId;
    private UserId userId;
    private Instant timestamp;
    private String faceImageKey;
    private boolean verified;

    public Vote() {}

    private Vote(Builder builder) {
        this.id = builder.id;
        this.pollId = builder.pollId;
        this.candidateId = builder.candidateId;
        this.userId = builder.userId;
        this.timestamp = builder.timestamp;
        this.faceImageKey = builder.faceImageKey;
        this.verified = builder.verified;
    }

    public static Builder builder() {
        return new Builder();
    }

    @DynamoDbPartitionKey
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getPollId() { return pollId != null ? pollId.value() : null; }
    public void setPollId(String pollId) { this.pollId = pollId != null ? PollId.of(pollId) : null; }

    public String getCandidateId() { return candidateId; }
    public void setCandidateId(String candidateId) { this.candidateId = candidateId; }

    public String getUserId() { return userId != null ? userId.value() : null; }
    public void setUserId(String userId) { this.userId = userId != null ? UserId.of(userId) : null; }

    public Instant getTimestamp() { return timestamp; }
    public void setTimestamp(Instant timestamp) { this.timestamp = timestamp; }

    public String getFaceImageKey() { return faceImageKey; }
    public void setFaceImageKey(String faceImageKey) { this.faceImageKey = faceImageKey; }

    public boolean isVerified() { return verified; }
    public void setVerified(boolean verified) { this.verified = verified; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vote vote)) return false;
        return Objects.equals(id, vote.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public static class Builder {
        private String id;
        private PollId pollId;
        private String candidateId;
        private UserId userId;
        private Instant timestamp;
        private String faceImageKey;
        private boolean verified;

        public Builder id(String id) { this.id = id; return this; }
        public Builder pollId(PollId pollId) { this.pollId = pollId; return this; }
        public Builder candidateId(String candidateId) { this.candidateId = candidateId; return this; }
        public Builder userId(UserId userId) { this.userId = userId; return this; }
        public Builder timestamp(Instant timestamp) { this.timestamp = timestamp; return this; }
        public Builder faceImageKey(String faceImageKey) { this.faceImageKey = faceImageKey; return this; }
        public Builder verified(boolean verified) { this.verified = verified; return this; }

        public Vote build() {
            if (id == null) id = UUID.randomUUID().toString();
            if (timestamp == null) timestamp = Instant.now();
            return new Vote(this);
        }
    }
}