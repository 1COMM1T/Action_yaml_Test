package com.commit.campus.service;

import com.commit.campus.dto.CampingDTO;
import com.commit.campus.dto.ReviewDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MyReviewService {
    Page<ReviewDTO> getMyReviews(long userId, Pageable pageable);

    Page<CampingDTO> getReviewedCampings(long userId, Pageable pageable);
}
