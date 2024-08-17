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
    public ResponseEntity<Void> generateDummyReviews(@RequestParam long user) {
        long userId = user;
        int campMaxCount = 3988;

        for (int i = 1; i < campMaxCount; i++) {
            CreateReviewRequest createReviewRequest = new CreateReviewRequest();
            long campId = i;
            createReviewRequest.setCampId(campId);
            createReviewRequest.setReviewContent("This is a dummy review content " + i);
            createReviewRequest.setRating((i % 5) + 1); // 1부터 5까지의 rating을 순환하며 생성
            createReviewRequest.setReviewImageUrl("http://example.com/image" + i + ".jpg");

            ReviewDTO reviewDTO = modelMapper.map(createReviewRequest, ReviewDTO.class);
            reviewDTO.setUserId(userId);
            reviewService.createReview(reviewDTO);
        }

        return ResponseEntity.noContent().build();
    }

}
