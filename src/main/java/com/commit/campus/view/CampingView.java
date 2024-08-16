package com.commit.campus.view;

import com.commit.campus.dto.CampingDTO;
import com.commit.campus.dto.CampingFacilitiesDTO;
import com.commit.campus.entity.Camping;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Schema(description = "캠핑 뷰 모델")
public class CampingView {
    @Schema(description = "캠핑장의 고유 ID")
    private Long campId;  // 캠핑장의 고유 ID
    @Schema(description = "캠핑장 이름")
    private String campName;  // 캠핑장 이름
    @Schema(description = "한줄 소개")
    private String lineIntro;  // 한줄 소개
    @Schema(description = "캠핑장 특징")
    private String featureSummary;  // 캠핑장 특징 요약
    @Schema(description = "주소")
    private String addr;  // 주소
    @Schema(description = "전화번호")
    private String tel;  // 전화번호
    @Schema(description = "홈페이지")
    private String homepage;  // 홈페이지 URL
    @Schema(description = "대표 이미지 URL")
    private String firstImageUrl;  // 대표 이미지 URL
    @Schema(description = "글램핑 사이트 수")
    private int glampingSiteCnt;  // 글램핑 사이트 수
    @Schema(description = "카라반 사이트 수")
    private int caravanSiteCnt;  // 카라반 사이트 수
    @Schema(description = "캠핑장 시설 목록")
    private List<CampingFacilitiesDTO> facilities;  // 캠핑장 시설 목록

    @Schema(description = "찜한 수")
    private int bookmarkCnt;  // 찜한 수

    @Schema(description = "리뷰 수")
    private int reviewCnt;  // 리뷰 수

    // Camping 엔티티 객체를 받아서 ViewModel 객체를 초기화하는 생성자
    public CampingView(Camping entity) {
        this.campId = entity.getCampId();
        this.campName = entity.getCampName();
        this.lineIntro = entity.getLineIntro();
        this.featureSummary = entity.getFeatureSummary();
        this.addr = entity.getAddr();
        this.tel = entity.getTel();
        this.homepage = entity.getHomepage();
        this.firstImageUrl = entity.getFirstImageUrl();
        this.glampingSiteCnt = entity.getGlampingSiteCnt();
        this.caravanSiteCnt = entity.getCaravanSiteCnt();
        if (entity.getCampingSummary() != null) {
            this.bookmarkCnt = entity.getCampingSummary().getBookmarkCnt();
            this.reviewCnt = entity.getCampingSummary().getReviewCnt();
        } else {
            this.bookmarkCnt = 0;
            this.reviewCnt = 0;
        }
        this.facilities = entity.getCampingFacilities().stream()
                .map(fac -> {
                    CampingFacilitiesDTO dto = new CampingFacilitiesDTO();
                    dto.setCampFacsId(fac.getCampFacsId());
                    dto.setCampId(fac.getCampId());
                    dto.setFacsTypeId(fac.getFacsTypeId());
                    dto.setInternalFacilitiesList(fac.getInternalFacilitiesList());
                    dto.setToiletCnt(fac.getToiletCnt());
                    dto.setShowerRoomCnt(fac.getShowerRoomCnt());
                    dto.setSinkCnt(fac.getSinkCnt());
                    dto.setBrazierClass(fac.getBrazierClass());
                    dto.setPersonalTrailerStatus(fac.getPersonalTrailerStatus());
                    dto.setPersonalCaravanStatus(fac.getPersonalCaravanStatus());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    // CampingDTO 객체를 받아서 ViewModel 객체를 초기화하는 생성자 추가
    public CampingView(CampingDTO dto) {
        this.campId = dto.getCampId();
        this.campName = dto.getCampName();
        this.lineIntro = dto.getLineIntro();
        this.featureSummary = dto.getFeatureSummary();
        this.addr = dto.getAddr();
        this.tel = dto.getTel();
        this.homepage = dto.getHomepage();
        this.firstImageUrl = dto.getFirstImageUrl();
        this.glampingSiteCnt = dto.getGlampingSiteCnt();
        this.caravanSiteCnt = dto.getCaravanSiteCnt();
        this.bookmarkCnt = dto.getBookmarkCnt();
        this.reviewCnt = dto.getReviewCnt();
        this.facilities = dto.getCampingFacilities().stream()
                .map(fac -> {
                    CampingFacilitiesDTO facilitiesDTO = new CampingFacilitiesDTO();
                    facilitiesDTO.setCampFacsId(fac.getCampFacsId());
                    facilitiesDTO.setCampId(fac.getCampId());
                    facilitiesDTO.setFacsTypeId(fac.getFacsTypeId());
                    facilitiesDTO.setInternalFacilitiesList(fac.getInternalFacilitiesList());
                    facilitiesDTO.setToiletCnt(fac.getToiletCnt());
                    facilitiesDTO.setShowerRoomCnt(fac.getShowerRoomCnt());
                    facilitiesDTO.setSinkCnt(fac.getSinkCnt());
                    facilitiesDTO.setBrazierClass(fac.getBrazierClass());
                    facilitiesDTO.setPersonalTrailerStatus(fac.getPersonalTrailerStatus());
                    facilitiesDTO.setPersonalCaravanStatus(fac.getPersonalCaravanStatus());
                    return facilitiesDTO;
                })
                .collect(Collectors.toList());
    }
}
