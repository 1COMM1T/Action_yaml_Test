package com.commit.campus.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.Date;

@Entity
@Getter
public class ReviewHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;  // 리뷰 ID

    @ManyToOne
    @JoinColumn(name = "camp_id")
    private Camping camping;  // 캠핑장 ID

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;  // 사용자 ID

    private String reviewContent;  // 리뷰 내용
    private int rating;  // 평점
    private Date reviewCreatedDate;  // 리뷰 생성 날짜
    private Date reviewModificationDate;  // 리뷰 수정 날짜
    private String reviewImageUrl;  // 리뷰 이미지 URL
}
