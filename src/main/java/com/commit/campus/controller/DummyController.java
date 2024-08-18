package com.commit.campus.controller;

import com.commit.campus.dto.CreateReviewRequest;
import com.commit.campus.dto.ReviewDTO;
import com.commit.campus.service.ReviewService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
@Slf4j
public class DummyController {

    private final ModelMapper modelMapper;
    private final ReviewService reviewService;

    public DummyController(ModelMapper modelMapper, ReviewService reviewService) {
        this.modelMapper = modelMapper;
        this.reviewService = reviewService;
    }

    // 리뷰 더미 데이터 생성
    @PostMapping("/generate-dummy")
    public ResponseEntity<Void> generateDummyReviews(
            @RequestParam int start,
            @RequestParam int campMaxCount,
            @RequestParam long user) {
        long userId = user;

        for (start = 1; start < campMaxCount; start++) {
            CreateReviewRequest createReviewRequest = new CreateReviewRequest();
            long campId = start;
            createReviewRequest.setCampId(campId);
            createReviewRequest.setReviewContent("This is a dummy review content " + start);
            createReviewRequest.setRating((start % 5) + 1); // 1부터 5까지의 rating을 순환하며 생성
            createReviewRequest.setReviewImageUrl("http://example.com/image" + start + ".jpg");

            ReviewDTO reviewDTO = modelMapper.map(createReviewRequest, ReviewDTO.class);
            reviewDTO.setUserId(userId);
            reviewService.createReview(reviewDTO);
        }

        return ResponseEntity.noContent().build();
    }

}
