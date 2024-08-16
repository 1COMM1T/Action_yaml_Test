package com.commit.campus.service;

import com.commit.campus.dto.CampingDTO;

import java.util.List;

public interface AdminService {
    void createCampground(CampingDTO campingDTO);

    List<CampingDTO> getAdminRegisteredCamping();

    void modifyCampground(CampingDTO campingDTO, Long campId);

    void deleteCampground(Long campId);

    void deleteReview(Long reviewId);
}
