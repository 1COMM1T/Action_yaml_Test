package com.commit.campus.controller;

import com.commit.campus.common.CustomResolver;
import com.commit.campus.dto.CreateReviewRequest;
import com.commit.campus.dto.ReviewDTO;
import com.commit.campus.dto.UpdateReviewRequest;
import com.commit.campus.entity.User;
import com.commit.campus.service.ReviewService;
import com.commit.campus.view.ReviewView;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/reviews")
@Slf4j
public class ReviewController {

    private final ReviewService reviewService;
    private final ModelMapper modelMapper;

    public ReviewController(ReviewService reviewService, ModelMapper modelMapper) {
        this.reviewService = reviewService;
        this.modelMapper = modelMapper;
    }

    // 캠핑장 리뷰 조회
    @GetMapping()
    public ResponseEntity<Page<ReviewView>> getReviewsByCampId(
            @RequestParam("campId") long campId,
            @PageableDefault(sort = "reviewCreatedDate", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<ReviewDTO> dtoPage = reviewService.getReviewsByCampId(campId, pageable);
        Page<ReviewView> viewPage = dtoPage.map(reviewDTO -> modelMapper.map(reviewDTO, ReviewView.class));

        return ResponseEntity.ok(viewPage);
    }

    // 리뷰 등록
    @PostMapping
    public ResponseEntity<Void> createReview(
            @RequestBody CreateReviewRequest createReviewRequest,
            @CustomResolver User authenticationUser) {

        long userId = authenticationUser.getUserId();

        ReviewDTO reviewDTO = modelMapper.map(createReviewRequest, ReviewDTO.class);
        reviewDTO.setUserId(userId);
        reviewService.createReview(reviewDTO);

        return ResponseEntity.noContent().build();
    }

    // 리뷰 수정
    @PutMapping
    public ResponseEntity<Void> updateReview(
            @RequestBody UpdateReviewRequest reviewRequest,
            @CustomResolver User authenticationUser) {

        long userId = authenticationUser.getUserId();
        ReviewDTO reviewDTO = modelMapper.map(reviewRequest, ReviewDTO.class);
        reviewService.updateReview(reviewDTO, userId);

        return ResponseEntity.noContent().build();
    }

    // 리뷰 삭제
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(
            @PathVariable long reviewId,
            @CustomResolver User authenticationUser) {

        long userId = authenticationUser.getUserId();
        reviewService.deleteReview(reviewId, userId);

        return ResponseEntity.noContent().build();
    }
}