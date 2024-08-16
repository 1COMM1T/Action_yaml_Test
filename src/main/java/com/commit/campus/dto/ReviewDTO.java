package com.commit.campus.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReviewDTO {
    private long reviewId;
    private long campId;
    private long userId;
    private String userNickname;
    private String reviewContent;
    private int rating;
    private LocalDateTime reviewCreatedDate;
    private LocalDateTime reviewModificationDate;
    private String reviewImageUrl;
}
