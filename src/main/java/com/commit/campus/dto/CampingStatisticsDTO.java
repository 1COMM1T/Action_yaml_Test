package com.commit.campus.dto;

import lombok.Data;

@Data
public class CampingStatisticsDTO {
    private Long campId;
    private int bookmarkCnt;
    private int reviewCnt;

    public CampingStatisticsDTO(Long campId, int bookmarkCnt, int reviewCnt) {
        this.campId = campId;
        this.bookmarkCnt = bookmarkCnt;
        this.reviewCnt = reviewCnt;
    }
}
