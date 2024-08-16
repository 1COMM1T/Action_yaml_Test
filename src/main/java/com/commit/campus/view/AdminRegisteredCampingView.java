package com.commit.campus.view;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class AdminRegisteredCampingView {
    private Long campId; // 캠핑장 ID
    private int contentId; // 콘텐츠 ID
    private String campName; // 야영장명
    private String lineIntro; // 한줄소개
    private String intro; // 소개
    private String doName; // 도
    private String sigunguName; // 시,군,구
    private String postCode; // 우편번호
    private String featureSummary; // 캠핑장 특징
    private String induty; // 업종
    private String addr; // 주소
    private String addrDetails; // 주소 상세
    private Double mapX; // 경도
    private Double mapY; // 위도
    private String tel; // 전화
    private String homepage; // 홈페이지
    private int staffCnt; // 상주관리인원
    private int generalSiteCnt; // 일반야영장개수
    private int carSiteCnt; // 자동차야영장개수
    private int glampingSiteCnt; // 글램핑개수
    private int caravanSiteCnt; // 카라반개수
    private int personalCaravanSiteCnt; // 개인카라반야영장개수
    private String supportFacilities; // 부대시설(편의시설)
    private String outdoorActivities; // 주변시설
    private String petAccess; // 애완동물출입
    private String rentalGearList; // 대여가능 캠핑장비리스트
    private String operationDay; // 운영일
    private String firstImageUrl; // 대표이미지
    private LocalDateTime createdDate; // 등록일
    private LocalDateTime modifiedDate; // 최종수정일

    public AdminRegisteredCampingView(Long campId, int contentId, String campName, String lineIntro, String intro, String doName, String sigunguName, String postCode, String featureSummary, String induty, String addr, String addrDetails, Double mapX, Double mapY, String tel, String homepage, int staffCnt, int generalSiteCnt, int carSiteCnt, int glampingSiteCnt, int caravanSiteCnt, int personalCaravanSiteCnt, String supportFacilities, String outdoorActivities, String petAccess, String rentalGearList, String operationDay, String firstImageUrl, LocalDateTime createdDate, LocalDateTime modifiedDate) {
        this.campId = campId;
        this.contentId = contentId;
        this.campName = campName;
        this.lineIntro = lineIntro;
        this.intro = intro;
        this.doName = doName;
        this.sigunguName = sigunguName;
        this.postCode = postCode;
        this.featureSummary = featureSummary;
        this.induty = induty;
        this.addr = addr;
        this.addrDetails = addrDetails;
        this.mapX = mapX;
        this.mapY = mapY;
        this.tel = tel;
        this.homepage = homepage;
        this.staffCnt = staffCnt;
        this.generalSiteCnt = generalSiteCnt;
        this.carSiteCnt = carSiteCnt;
        this.glampingSiteCnt = glampingSiteCnt;
        this.caravanSiteCnt = caravanSiteCnt;
        this.personalCaravanSiteCnt = personalCaravanSiteCnt;
        this.supportFacilities = supportFacilities;
        this.outdoorActivities = outdoorActivities;
        this.petAccess = petAccess;
        this.rentalGearList = rentalGearList;
        this.operationDay = operationDay;
        this.firstImageUrl = firstImageUrl;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }
}
