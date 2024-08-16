package com.commit.campus.dto;

import lombok.Data;

@Data
public class UpdateReviewRequest {
    private long reviewId;
    private String reviewContent;
    private int rating;
    private String reviewImageUrl;

}
