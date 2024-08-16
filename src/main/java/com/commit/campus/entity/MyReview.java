package com.commit.campus.entity;

import com.commit.campus.common.converter.LongListConverter;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "my_review")
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
public class MyReview {

    @Id
    @Column(name = "user_id")
    private long userId;

    @Column(name = "review_count", nullable = false)
    private int reviewCount;

    @Convert(converter = LongListConverter.class)
    @Column(name = "review_ids", columnDefinition = "json")
    private List<Long> reviewIds;

    @Column(name = "last_review_date")
    private LocalDateTime lastReviewDate;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public MyReview(Long userId) {
        this.userId = userId;
        this.reviewCount = 0;
        this.reviewIds = new ArrayList<>();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PrePersist
    protected void onCreate() {
        if (this.reviewIds == null) {
            this.reviewIds = new ArrayList<>();
        }
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
        this.updatedAt = LocalDateTime.now();
    }

    public void incrementReviewCnt(Long reviewId) {
        this.reviewCount++;
        this.reviewIds.add(reviewId);
        this.lastReviewDate = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void decrementReviewCnt(Long reviewId) {
        this.reviewIds.remove(reviewId);
        this.reviewCount--;
        this.updatedAt = LocalDateTime.now();
        if (this.reviewIds.isEmpty()) {
            this.lastReviewDate = null;
        }
    }
}
