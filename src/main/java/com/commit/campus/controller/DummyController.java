package com.commit.campus.controller;

import com.commit.campus.dto.CreateReviewRequest;
import com.commit.campus.dto.ReviewDTO;
import com.commit.campus.service.ReviewService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Random;

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
    public ResponseEntity<Void> generateDummyReviews(@RequestParam int count, @RequestParam long user) {
        log.info("count, user : ", count, user);
        long userId = user;
        Random random = new Random();
        for (int i = 0; i < count; i++) {
            CreateReviewRequest createReviewRequest = new CreateReviewRequest();
            // 3부터 3930까지의 camp_id를 랜덤하게 생성
            long campId = 3 + random.nextInt(3928); // 3 + (0부터 3927까지의 난수)
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
