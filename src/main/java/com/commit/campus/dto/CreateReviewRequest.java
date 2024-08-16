package com.commit.campus.dto;

import lombok.Data;

@Data
public class CreateReviewRequest {
    private Long campId;
    private String reviewContent;
    private int rating;
    private String reviewImageUrl;
}
