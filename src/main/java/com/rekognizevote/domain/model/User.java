package com.rekognizevote.domain.model;

import com.rekognizevote.domain.valueobject.Email;
import com.rekognizevote.domain.valueobject.UserId;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

import java.time.Instant;
import java.util.Objects;

@DynamoDbBean
public class User {
    private UserId id;
    private String name;
    private Email email;
    private String passwordHash;
    private byte[] faceVector;
    private Instant createdAt;
    private Instant lastLoginAt;
    private boolean active;

    public User() {}

    private User(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.email = builder.email;
        this.passwordHash = builder.passwordHash;
        this.faceVector = builder.faceVector;
        this.createdAt = builder.createdAt;
        this.lastLoginAt = builder.lastLoginAt;
        this.active = builder.active;
    }

    public static Builder builder() {
        return new Builder();
    }

    public User updateLastLogin() {
        return builder()
                .from(this)
                .lastLoginAt(Instant.now())
                .build();
    }

    @DynamoDbPartitionKey
    public String getId() {
        return id != null ? id.value() : null;
    }

    public void setId(String id) {
        this.id = id != null ? UserId.of(id) : null;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() {
        return email != null ? email.value() : null;
    }

    public void setEmail(String email) {
        this.email = email != null ? Email.of(email) : null;
    }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public byte[] getFaceVector() { return faceVector; }
    public void setFaceVector(byte[] faceVector) { this.faceVector = faceVector; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Instant getLastLoginAt() { return lastLoginAt; }
    public void setLastLoginAt(Instant lastLoginAt) { this.lastLoginAt = lastLoginAt; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public static class Builder {
        private UserId id;
        private String name;
        private Email email;
        private String passwordHash;
        private byte[] faceVector;
        private Instant createdAt;
        private Instant lastLoginAt;
        private boolean active = true;

        public Builder id(UserId id) { this.id = id; return this; }
        public Builder name(String name) { this.name = name; return this; }
        public Builder email(Email email) { this.email = email; return this; }
        public Builder passwordHash(String passwordHash) { this.passwordHash = passwordHash; return this; }
        public Builder faceVector(byte[] faceVector) { this.faceVector = faceVector; return this; }
        public Builder createdAt(Instant createdAt) { this.createdAt = createdAt; return this; }
        public Builder lastLoginAt(Instant lastLoginAt) { this.lastLoginAt = lastLoginAt; return this; }
        public Builder active(boolean active) { this.active = active; return this; }

        public Builder from(User user) {
            this.id = user.id;
            this.name = user.name;
            this.email = user.email;
            this.passwordHash = user.passwordHash;
            this.faceVector = user.faceVector;
            this.createdAt = user.createdAt;
            this.lastLoginAt = user.lastLoginAt;
            this.active = user.active;
            return this;
        }

        public User build() {
            if (createdAt == null) createdAt = Instant.now();
            return new User(this);
        }
    }
}