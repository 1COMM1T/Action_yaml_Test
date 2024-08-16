package com.commit.campus.controller;

import com.commit.campus.dto.CampingDTO;
import com.commit.campus.service.AdminService;
import com.commit.campus.view.AdminRegisteredCampingView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/internal/v1")
@Slf4j
public class AdminController {

    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping
    public ResponseEntity<List<AdminRegisteredCampingView>> getAdminRegisteredCamping() {
        List<CampingDTO> campingDTOList = adminService.getAdminRegisteredCamping();

        List<AdminRegisteredCampingView> adminRegisteredCampingViewList = campingDTOList.stream()
                .map(campingDTO -> AdminRegisteredCampingView.builder()
                        .campId(campingDTO.getCampId())
                        .contentId(campingDTO.getContentId())
                        .campName(campingDTO.getCampName())
                        .lineIntro(campingDTO.getLineIntro())
                        .intro(campingDTO.getIntro())
                        .doName(campingDTO.getDoName())
                        .sigunguName(campingDTO.getSigunguName())
                        .postCode(campingDTO.getPostCode())
                        .featureSummary(campingDTO.getFeatureSummary())
                        .induty(campingDTO.getInduty())
                        .addr(campingDTO.getAddr())
                        .addrDetails(campingDTO.getAddrDetails())
                        .mapX(campingDTO.getMapX())
                        .mapY(campingDTO.getMapY())
                        .tel(campingDTO.getTel())
                        .homepage(campingDTO.getHomepage())
                        .staffCnt(campingDTO.getStaffCnt())
                        .generalSiteCnt(campingDTO.getGeneralSiteCnt())
                        .carSiteCnt(campingDTO.getCarSiteCnt())
                        .glampingSiteCnt(campingDTO.getGlampingSiteCnt())
                        .caravanSiteCnt(campingDTO.getCaravanSiteCnt())
                        .personalCaravanSiteCnt(campingDTO.getPersonalCaravanSiteCnt())
                        .createdDate(campingDTO.getCreatedDate())
                        .modifiedDate(campingDTO.getModifiedDate())
                        .supportFacilities(campingDTO.getSupportFacilities())
                        .outdoorActivities(campingDTO.getOutdoorActivities())
                        .petAccess(campingDTO.getPetAccess())
                        .rentalGearList(campingDTO.getRentalGearList())
                        .operationDay(campingDTO.getOperationDay())
                        .firstImageUrl(campingDTO.getFirstImageUrl())
                        .build())
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(adminRegisteredCampingViewList);
    }

    @PostMapping
    public ResponseEntity<Void> createCampground(@RequestBody CampingDTO campingDTO) {
        adminService.createCampground(campingDTO);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{campId}")
    public ResponseEntity<Void> modifyCampground(@RequestBody CampingDTO campingDTO, @PathVariable Long campId) {
        adminService.modifyCampground(campingDTO, campId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/camp/{campId}")
    public ResponseEntity<Void> deleteCampground(@PathVariable Long campId) {
        adminService.deleteCampground(campId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/review/{reviewId}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long reviewId) {
        adminService.deleteReview(reviewId);
        return ResponseEntity.ok().build();
    }
}
