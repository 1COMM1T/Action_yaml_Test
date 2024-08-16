package com.commit.campus.controller;

import com.commit.campus.common.CustomResolver;
import com.commit.campus.dto.CampingDTO;
import com.commit.campus.dto.ReviewDTO;
import com.commit.campus.entity.User;
import com.commit.campus.service.MyReviewService;
import com.commit.campus.service.ReviewService;
import com.commit.campus.view.MyReviewView;
import com.commit.campus.view.ReviewedCampingView;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/myReview")
public class MyReviewController {

    private final MyReviewService myReviewService;
    private final ReviewService reviewService;
    private final ModelMapper modelMapper;

    public MyReviewController(MyReviewService myReviewService, ReviewService reviewService, ModelMapper modelMapper) {
        this.myReviewService = myReviewService;
        this.reviewService = reviewService;
        this.modelMapper = modelMapper;
    }

    // 내 리뷰 정보 조회 TODO:레디스 캐시 사용
    @GetMapping
    public ResponseEntity<Page<MyReviewView>> getMyReviews(
            @PageableDefault(sort = "reviewCreatedDate") Pageable pageable,
            @CustomResolver User authenticationUser) {

        long userId = authenticationUser.getUserId();

        Page<ReviewDTO> dtoPage = myReviewService.getMyReviews(userId, pageable);

        Page<MyReviewView> viewPage = dtoPage.map(reviewDTO -> modelMapper.map(reviewDTO, MyReviewView.class));
        return ResponseEntity.ok(viewPage);
    }

    // 리뷰 작성한 캠핑장 조회
    @GetMapping("/campings")
    public ResponseEntity<Page<ReviewedCampingView>> getReviewedCampings(
            @PageableDefault(sort = "campId") Pageable pageable,
            @CustomResolver User authenticationUser) {

        long userId = authenticationUser.getUserId();

        Page<CampingDTO> dtoPage = myReviewService.getReviewedCampings(userId, pageable);

        Page<ReviewedCampingView> viewPage = dtoPage.map(campingDTO -> modelMapper.map(campingDTO, ReviewedCampingView.class));

        return ResponseEntity.ok(viewPage);
    }
}
