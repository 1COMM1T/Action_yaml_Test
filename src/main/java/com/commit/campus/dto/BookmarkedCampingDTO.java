package com.commit.campus.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BookmarkedCampingDTO {

    private String campName;
    private String doName;
    private String sigunguName;
    private String postCode;
    private String induty;
    private String firstImageUrl;

    public BookmarkedCampingDTO(String campName, String doName, String sigunguName, String postCode, String induty, String firstImageUrl) {
        this.campName = campName;
        this.doName = doName;
        this.sigunguName = sigunguName;
        this.postCode = postCode;
        this.induty = induty;
        this.firstImageUrl = firstImageUrl;
    }
}
