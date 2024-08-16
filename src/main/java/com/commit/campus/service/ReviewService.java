package com.commit.campus.service;

import com.commit.campus.common.exceptions.NotAuthorizedException;
import com.commit.campus.common.exceptions.ReviewAlreadyExistsException;
import com.commit.campus.dto.ReviewDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReviewService {
    Page<ReviewDTO> getReviewsByCampId(long campId, Pageable pageable);

    void createReview(ReviewDTO reviewDTO) throws ReviewAlreadyExistsException;

    void updateReview(ReviewDTO reviewDTO, long userId);

    void deleteReview(long reviewId, long userId) throws NotAuthorizedException;
}
