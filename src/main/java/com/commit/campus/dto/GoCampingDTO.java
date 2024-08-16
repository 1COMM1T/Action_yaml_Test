package com.commit.campus.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class GoCampingDTO {
    // campingEntity에 들어갈 데이터

    @JsonProperty("contentId")
    private int contentId;    // 콘텐트 ID

    @JsonProperty("facltNm")
    private String campName;  // 야영장명

    @JsonProperty("lineIntro")
    private String lineIntro;  // 한줄소개

    @JsonProperty("intro")
    private String intro;  // 소개

    @JsonProperty("doNm")
    private String doName;  // 도

    @JsonProperty("sigunguNm")
    private String sigunguName;  // 시군구

    @JsonProperty("zipcode")
    private String postCode;  // 우편번호

    @JsonProperty("featureNm")
    private String featureSummary;  // 특징명

    @JsonProperty("induty")
    private String induty;  // 업종

    @JsonProperty("addr1")
    private String addr;  // 주소

    @JsonProperty("addr2")
    private String addrDetails;  // 주소 상세

    @JsonProperty("mapX")
    private double mapX;  // 경도

    @JsonProperty("mapY")
    private double mapY;  // 위도

    @JsonProperty("tel")
    private String tel;  // 전화

    @JsonProperty("homepage")
    private String homepage;  // 홈페이지

    @JsonProperty("manageNmpr")
    private int staffCount;  // 상주관리인원

    @JsonProperty("createdtime")
    private LocalDateTime createdDate;  // 등록일

    @JsonProperty("modifiedtime")
    private LocalDateTime modifiedDate; // 수정일

    @JsonProperty("gnrlSiteCo")
    private Integer general_site_cnt;   // 주요시설 일반야영장

    @JsonProperty("autoSiteCo")
    private Integer car_site_cnt;       // 주요시설 자동차 야영장

    @JsonProperty("glampSiteCo")
    private Integer glamping_site_cnt;  // 주요시설 글램핑

    @JsonProperty("caravSiteCo")
    private Integer caravan_site_cnt;   // 주요시설 카라반

    @JsonProperty("indvdlCaravSiteCo")
    private Integer personal_caravan_site_cnt;  // 주요시설 개인카라반

    @JsonProperty("sbrsCl")
    private String supportFacilities;   // 편의 시설

    @JsonProperty("posblFcltyCl")
    private String outdoorActivities;   // 주변 이용가능 시설

    @JsonProperty("animalCmgCl")
    private String petAccess;           // 반려동물동반여부

    @JsonProperty("operDeCl")
    private String operationDay;        // 운영일

    @JsonProperty("eqpmnLendCl")
    private String rentalGearList;          // 장비 대여 리스트

    // campingFacilitiesEntity에 들어갈 데이터
    @JsonProperty("glampInnerFclty")
    private String internalFacilitiesList; // 부대시설

    @JsonProperty("toiletCo")
    private int toiletCnt;      // 화장실 개수

    @JsonProperty("swrmCo")
    private int showerRoomCnt;  // 샤워실 개수

    @JsonProperty("btrpCO")
    private int sinkCnt;        // 싱크대 개수

    @JsonProperty("braizerCl")
    private String brazierClass;    // 화로대 개수

    @JsonProperty("firstImageUrl")
    private String firstImageUrl;       // 대표이미지

    @JsonProperty("trlerAcmpnyAt")
    private String personalTrailerStatus;   // 개인 트레일러 동반 여부

    @JsonProperty("caravAcmpnyAt")
    private String personalCaravanStatus;   // 개인 카라반 동반 여부

}
