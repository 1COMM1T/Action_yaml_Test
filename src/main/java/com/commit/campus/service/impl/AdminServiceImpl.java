package com.commit.campus.service.impl;

import com.commit.campus.dto.CampingDTO;
import com.commit.campus.entity.Camping;
import com.commit.campus.repository.CampingRepository;
import com.commit.campus.repository.ReviewRepository;
import com.commit.campus.service.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AdminServiceImpl implements AdminService {

    private final CampingRepository campingRepository;
    private final ReviewRepository reviewRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public AdminServiceImpl(CampingRepository campingRepository, ReviewRepository reviewRepository, ModelMapper modelMapper) {
        this.campingRepository = campingRepository;
        this.reviewRepository = reviewRepository;
        this.modelMapper = modelMapper;
    }


    @Override
    @Transactional
    public void createCampground(CampingDTO campingDTO) {
        LocalDateTime now = LocalDateTime.now();

        Camping camping = modelMapper.map(campingDTO, Camping.class);
        camping.setCreatedDate(now);
        camping.setLastModifiedDate(now);

        campingRepository.save(camping);
    }

    @Override
    public List<CampingDTO> getAdminRegisteredCamping() {
        List<Camping> campings = campingRepository.findByContentId(0);

        return campings.stream()
                .map(camping -> modelMapper.map(camping, CampingDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void modifyCampground(CampingDTO campingDTO, Long campId) {
        LocalDateTime now = LocalDateTime.now();
        Camping updateCamping = campingRepository.findById(campId).orElseThrow(IllegalArgumentException::new);

        updateCamping.setCampName(campingDTO.getCampName());
        updateCamping.setLineIntro(campingDTO.getLineIntro());
        updateCamping.setIntro(campingDTO.getIntro());
        updateCamping.setDoName(campingDTO.getDoName());
        updateCamping.setSigunguName(campingDTO.getSigunguName());
        updateCamping.setPostCode(campingDTO.getPostCode());
        updateCamping.setFeatureSummary(campingDTO.getFeatureSummary());
        updateCamping.setInduty(campingDTO.getInduty());
        updateCamping.setAddr(campingDTO.getAddr());
        updateCamping.setAddrDetails(campingDTO.getAddrDetails());
        updateCamping.setMapX(campingDTO.getMapX());
        updateCamping.setMapY(campingDTO.getMapY());
        updateCamping.setTel(campingDTO.getTel());
        updateCamping.setHomepage(campingDTO.getHomepage());
        updateCamping.setStaffCount(campingDTO.getStaffCnt());
        updateCamping.setGeneralSiteCnt(campingDTO.getGeneralSiteCnt());
        updateCamping.setCarSiteCnt(campingDTO.getCarSiteCnt());
        updateCamping.setGlampingSiteCnt(campingDTO.getGlampingSiteCnt());
        updateCamping.setCaravanSiteCnt(campingDTO.getCaravanSiteCnt());
        updateCamping.setPersonalCaravanSiteCnt(campingDTO.getPersonalCaravanSiteCnt());
        updateCamping.setLastModifiedDate(now);
        updateCamping.setSupportFacilities(campingDTO.getSupportFacilities());
        updateCamping.setOutdoorActivities(campingDTO.getOutdoorActivities());
        updateCamping.setPetAccess(campingDTO.getPetAccess());
        updateCamping.setRentalGearList(campingDTO.getRentalGearList());
        updateCamping.setOperationDay(campingDTO.getOperationDay());
        updateCamping.setFirstImageUrl(campingDTO.getFirstImageUrl());
    }

    @Override
    @Transactional
    public void deleteCampground(Long campId) {
        campingRepository.deleteById(campId);
    }

    @Override
    @Transactional
    public void deleteReview(Long reviewId) {
        reviewRepository.deleteById(reviewId);
    }

}
