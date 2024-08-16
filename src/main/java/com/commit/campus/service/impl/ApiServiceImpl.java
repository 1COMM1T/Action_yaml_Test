package com.commit.campus.service.impl;

import com.commit.campus.common.openfeign.CampingApiClient;
import com.commit.campus.dto.GoCampingDTO;
import com.commit.campus.entity.Camping;
import com.commit.campus.entity.CampingFacilities;
import com.commit.campus.repository.CampingFacilitiesRepository;
import com.commit.campus.repository.CampingRepository;
import com.commit.campus.service.ApiService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ApiServiceImpl implements ApiService {

    private final CampingApiClient campingApiClient;
    private final ObjectMapper objectMapper;
    private final CampingRepository campingRepository;
    private final CampingFacilitiesRepository campingFacilitiesRepository;

    public ApiServiceImpl(
            CampingApiClient campingApiClient,
            ObjectMapper objectMapper, CampingRepository campingRepository,
            CampingFacilitiesRepository campingFacilitiesRepository) {
        this.campingApiClient = campingApiClient;
        this.objectMapper = objectMapper;
        this.campingRepository = campingRepository;
        this.campingFacilitiesRepository = campingFacilitiesRepository;
    }

    @Value("${gocamping.api.encoding-key}")
    private String serviceKey;

    private static final int NUM_OF_ROWS = 5000;
    private static final int PAGE_NO = 0;
    private static final String VALIDATION_CHECK_OS_KIND = "ETC";
    private static final String VALIDATION_CHECK_APP_NAME = "campus";
    private static final String RESPONSE_FIFE_FORMAT = "json";

    @Override
    public String callCampingApi() {
        String responseJson =
                campingApiClient.getBaseList(
                        NUM_OF_ROWS, PAGE_NO,
                        VALIDATION_CHECK_OS_KIND,
                        VALIDATION_CHECK_APP_NAME,
                        serviceKey, RESPONSE_FIFE_FORMAT);

        return responseJson;
    }

    @Override
    public void saveCampingData() {
        try {
            String campingData = callCampingApi();

            JsonNode rootNode = objectMapper.readTree(campingData);
            JsonNode itemsNode = rootNode.path("response").path("body").path("items").path("item");

            List<GoCampingDTO> campingDTOList = objectMapper.convertValue(itemsNode, new TypeReference<List<GoCampingDTO>>() {});

            // 테이블 초기화
            campingFacilitiesRepository.deleteAll();
            campingRepository.deleteAll();

            for (GoCampingDTO campingDTO : campingDTOList) {

                Camping campingEntity = mapToEntity(campingDTO);
                campingRepository.save(campingEntity);

                List<CampingFacilities> facilities = checkCampFacsType(campingEntity, campingDTO);
                campingFacilitiesRepository.saveAll(facilities);
            }
        } catch (Exception e) {
            log.error("Error while saving camping data", e);
        }
    }

    private Camping mapToEntity(GoCampingDTO campingDTO) {

        Camping campingEntity = new Camping();

        campingEntity.setContentId(campingDTO.getContentId());
        campingEntity.setCampName(campingDTO.getCampName());
        campingEntity.setLineIntro(campingDTO.getLineIntro());
        campingEntity.setIntro(campingDTO.getIntro());
        campingEntity.setDoName(campingDTO.getDoName());
        campingEntity.setSigunguName(campingDTO.getSigunguName());
        campingEntity.setPostCode(campingDTO.getPostCode());
        campingEntity.setFeatureSummary(campingDTO.getFeatureSummary());
        campingEntity.setInduty(campingDTO.getInduty());
        campingEntity.setAddr(campingDTO.getAddr());
        campingEntity.setAddr(campingDTO.getAddrDetails());
        campingEntity.setMapX(campingDTO.getMapX());
        campingEntity.setMapY(campingDTO.getMapY());
        campingEntity.setTel(campingDTO.getTel());
        campingEntity.setHomepage(campingDTO.getHomepage());
        campingEntity.setStaffCount(campingDTO.getStaffCount());
        campingEntity.setCreatedDate(campingDTO.getCreatedDate());
        campingEntity.setLastModifiedDate(campingDTO.getModifiedDate());
        campingEntity.setGeneralSiteCnt(campingDTO.getGeneral_site_cnt());
        campingEntity.setCarSiteCnt(campingDTO.getCar_site_cnt());
        campingEntity.setGlampingSiteCnt(campingDTO.getGlamping_site_cnt());
        campingEntity.setCaravanSiteCnt(campingDTO.getCaravan_site_cnt());
        campingEntity.setPersonalCaravanSiteCnt(campingDTO.getPersonal_caravan_site_cnt());
        campingEntity.setSupportFacilities(campingDTO.getSupportFacilities());
        campingEntity.setOutdoorActivities(campingDTO.getOutdoorActivities());
        campingEntity.setPetAccess(campingDTO.getPetAccess());
        campingEntity.setRentalGearList(campingDTO.getRentalGearList());
        campingEntity.setOperationDay(campingDTO.getOperationDay());
        campingEntity.setFirstImageUrl(campingDTO.getFirstImageUrl());

        return campingEntity;

    }

    private List<CampingFacilities> checkCampFacsType(Camping campingEntity, GoCampingDTO campingDTO) {

        List<CampingFacilities> facilities = new ArrayList<>();

        if (campingDTO.getGeneral_site_cnt() > 0) {
            facilities.add(createFacility(campingEntity, campingDTO, 1)); // 1, 일반야영장
        }
        if (campingDTO.getCar_site_cnt() > 0) {
            facilities.add(createFacility(campingEntity, campingDTO, 2)); // 2, 자동차 야영장
        }
        if (campingDTO.getGlamping_site_cnt() > 0) {
            facilities.add(createFacility(campingEntity, campingDTO, 3)); // 3, 글램핑
        }
        if (campingDTO.getCaravan_site_cnt() > 0) {
            facilities.add(createFacility(campingEntity, campingDTO, 4)); // 4, 카라반
        }
        if (campingDTO.getPersonal_caravan_site_cnt() > 0) {
            facilities.add(createFacility(campingEntity, campingDTO, 5)); // 5, 개인카라반
        }

        return facilities;
    }

    private CampingFacilities createFacility(Camping camping, GoCampingDTO campingDTO, int facsTypeId) {

        CampingFacilities facilitiesEntity = new CampingFacilities();

        facilitiesEntity.setCampId(camping.getCampId());
        facilitiesEntity.setFacsTypeId(facsTypeId);
        facilitiesEntity.setInternalFacilitiesList(campingDTO.getInternalFacilitiesList());
        facilitiesEntity.setToiletCnt(campingDTO.getToiletCnt());
        facilitiesEntity.setShowerRoomCnt(campingDTO.getShowerRoomCnt());
        facilitiesEntity.setSinkCnt(campingDTO.getSinkCnt());
        facilitiesEntity.setBrazierClass(campingDTO.getBrazierClass());
        facilitiesEntity.setPersonalTrailerStatus(campingDTO.getPersonalTrailerStatus());
        facilitiesEntity.setPersonalCaravanStatus(campingDTO.getPersonalCaravanStatus());

        return facilitiesEntity;
    }
}
