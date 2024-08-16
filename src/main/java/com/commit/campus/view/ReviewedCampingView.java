package com.commit.campus.view;

import lombok.Data;

@Data
public class ReviewedCampingView {
    private Long campId; // 콘텐츠 ID
    private String campName; // 야영장명
    private String firstImageUrl; // 대표이미지
}
