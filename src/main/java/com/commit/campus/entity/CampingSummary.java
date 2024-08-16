package com.commit.campus.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name="camping_summary")
@Getter
@Builder
@ToString
@NoArgsConstructor
@DynamicUpdate
public class CampingSummary {

    @Id
    @Column(name = "camp_id")
    private Long campId;

    @Column(nullable = false, name = "bookmark_cnt")
    private int bookmarkCnt;

    @Column(nullable = false, name = "review_cnt")
    private int reviewCnt;

    public CampingSummary(Long campId, int bookmarkCnt, int reviewCnt) {
        this.campId = campId;
        this.bookmarkCnt = bookmarkCnt;
        this.reviewCnt = reviewCnt;
    }

    public void incrementReviewCnt() {
        this.reviewCnt++;
    }

    public void decrementReviewCnt() {
        if (this.reviewCnt > 0) {
            this.reviewCnt--;
        }
    }
}
