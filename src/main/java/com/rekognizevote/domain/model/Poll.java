package com.rekognizevote.domain.model;

import com.rekognizevote.domain.valueobject.PollId;
import com.rekognizevote.domain.valueobject.PollStatus;
import com.rekognizevote.domain.valueobject.UserId;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

@DynamoDbBean
public class Poll {
    private PollId id;
    private String title;
    private String description;
    private PollStatus status;
    private boolean isPrivate;
    private String accessCode;
    private Instant startDate;
    private Instant endDate;
    private List<Candidate> candidates;
    private UserId createdBy;
    private Instant createdAt;

    public Poll() {}

    private Poll(Builder builder) {
        this.id = builder.id;
        this.title = builder.title;
        this.description = builder.description;
        this.status = builder.status;
        this.isPrivate = builder.isPrivate;
        this.accessCode = builder.accessCode;
        this.startDate = builder.startDate;
        this.endDate = builder.endDate;
        this.candidates = builder.candidates;
        this.createdBy = builder.createdBy;
        this.createdAt = builder.createdAt;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Poll updateStatus() {
        Instant now = Instant.now();
        PollStatus newStatus = status;
        
        if (now.isBefore(startDate)) {
            newStatus = PollStatus.UPCOMING;
        } else if (now.isAfter(endDate)) {
            newStatus = PollStatus.CLOSED;
        } else {
            newStatus = PollStatus.ACTIVE;
        }
        
        return builder().from(this).status(newStatus).build();
    }

    public int getTotalVotes() {
        return candidates.stream().mapToInt(Candidate::voteCount).sum();
    }

    public boolean canUserVote() {
        return status.canVote() && Instant.now().isBefore(endDate);
    }

    @DynamoDbPartitionKey
    public String getId() {
        return id != null ? id.value() : null;
    }

    public void setId(String id) {
        this.id = id != null ? PollId.of(id) : null;
    }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getStatus() { return status != null ? status.name() : null; }
    public void setStatus(String status) { this.status = status != null ? PollStatus.valueOf(status) : null; }

    public boolean isPrivate() { return isPrivate; }
    public void setPrivate(boolean isPrivate) { this.isPrivate = isPrivate; }

    public String getAccessCode() { return accessCode; }
    public void setAccessCode(String accessCode) { this.accessCode = accessCode; }

    public Instant getStartDate() { return startDate; }
    public void setStartDate(Instant startDate) { this.startDate = startDate; }

    public Instant getEndDate() { return endDate; }
    public void setEndDate(Instant endDate) { this.endDate = endDate; }

    public List<Candidate> getCandidates() { return candidates; }
    public void setCandidates(List<Candidate> candidates) { this.candidates = candidates; }

    public String getCreatedBy() { return createdBy != null ? createdBy.value() : null; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy != null ? UserId.of(createdBy) : null; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Poll poll)) return false;
        return Objects.equals(id, poll.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public static class Builder {
        private PollId id;
        private String title;
        private String description;
        private PollStatus status = PollStatus.UPCOMING;
        private boolean isPrivate;
        private String accessCode;
        private Instant startDate;
        private Instant endDate;
        private List<Candidate> candidates;
        private UserId createdBy;
        private Instant createdAt;

        public Builder id(PollId id) { this.id = id; return this; }
        public Builder title(String title) { this.title = title; return this; }
        public Builder description(String description) { this.description = description; return this; }
        public Builder status(PollStatus status) { this.status = status; return this; }
        public Builder isPrivate(boolean isPrivate) { this.isPrivate = isPrivate; return this; }
        public Builder accessCode(String accessCode) { this.accessCode = accessCode; return this; }
        public Builder startDate(Instant startDate) { this.startDate = startDate; return this; }
        public Builder endDate(Instant endDate) { this.endDate = endDate; return this; }
        public Builder candidates(List<Candidate> candidates) { this.candidates = candidates; return this; }
        public Builder createdBy(UserId createdBy) { this.createdBy = createdBy; return this; }
        public Builder createdAt(Instant createdAt) { this.createdAt = createdAt; return this; }

        public Builder from(Poll poll) {
            this.id = poll.id;
            this.title = poll.title;
            this.description = poll.description;
            this.status = poll.status;
            this.isPrivate = poll.isPrivate;
            this.accessCode = poll.accessCode;
            this.startDate = poll.startDate;
            this.endDate = poll.endDate;
            this.candidates = poll.candidates;
            this.createdBy = poll.createdBy;
            this.createdAt = poll.createdAt;
            return this;
        }

        public Poll build() {
            if (createdAt == null) createdAt = Instant.now();
            return new Poll(this);
        }
    }
}