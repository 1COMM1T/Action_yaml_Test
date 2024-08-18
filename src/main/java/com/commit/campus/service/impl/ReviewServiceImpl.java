package com.commit.campus.service.impl;

import com.commit.campus.common.exceptions.NotAuthorizedException;
import com.commit.campus.common.exceptions.ReviewAlreadyExistsException;
import com.commit.campus.common.exceptions.ReviewNotFoundException;
import com.commit.campus.dto.ReviewDTO;
import com.commit.campus.entity.CampingSummary;
import com.commit.campus.entity.MyReview;
import com.commit.campus.entity.Review;
import com.commit.campus.repository.*;
import com.commit.campus.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final MyReviewRepository myReviewRepository;
    private final UserRepository userRepository;
    private final RatingSummaryRepository ratingSummaryRepository;
    private final CampingSummaryRepository campingSummaryRepository;
    private final ModelMapper modelMapper;

    @Override
    public Page<ReviewDTO> getReviewsByCampId(long campId, Pageable pageable) {

        Page<Review> reviewPage = reviewRepository.findByCampId(campId, pageable);

        return reviewPage.map(this::mapToReviewWithNickname);
    }

    @Override
    @Transactional
    public void createReview(ReviewDTO reviewDTO) throws ReviewAlreadyExistsException {
        log.info("리뷰 생성 시작 - 유저ID: {}, 캠핑장ID: {}", reviewDTO.getUserId(), reviewDTO.getCampId());

        checkExistingReview(reviewDTO.getUserId(), reviewDTO.getCampId());

        Review savedReview = saveReview(reviewDTO);
        log.info("리뷰 저장 완료 - 리뷰ID: {}", savedReview.getReviewId());

        updateMyReview(savedReview.getUserId(), savedReview.getReviewId(), true);
        log.info("내 리뷰 업데이트 완료 - 유저ID: {}, 리뷰ID: {}", savedReview.getUserId(), savedReview.getReviewId());

        updateRating(savedReview.getCampId(), savedReview.getRating(), true);
        log.info("캠핑장 평점 업데이트 완료 - 캠핑장ID: {}, 평점: {}", savedReview.getCampId(), savedReview.getRating());

        incrementReviewCnt(savedReview.getCampId());
        log.info("캠핑장 리뷰 카운트 증가 완료 - 캠핑장ID: {}", savedReview.getCampId());
    }

    @Override
    @Transactional
    public void updateReview(ReviewDTO reviewDTO, long userId) {

        Review oldReview = findReviewById(reviewDTO.getReviewId());
        int oldRating = oldReview.getRating();

        verifyReviewPermission(oldReview.getUserId(), userId, "수정");

        Review newReview = updateReviewFromDTO(oldReview, reviewDTO);
        reviewRepository.save(newReview);

        adjustRating(oldRating, newReview.getRating(), newReview.getCampId());
    }

    @Override
    @Transactional
    public void deleteReview(long reviewId, long userId) {

        Review review = findReviewById(reviewId);
        verifyReviewPermission(review.getUserId(), userId, "삭제");

        decrementReviewCnt(review.getCampId());
        updateRating(review.getCampId(), review.getRating(), false);
        updateMyReview(userId, reviewId, false);

        reviewRepository.delete(review);
    }

    private ReviewDTO mapToReviewWithNickname(Review review) {

        ReviewDTO reviewDTO = modelMapper.map(review, ReviewDTO.class);
        String userNickname = userRepository.findNicknameByUserId(review.getUserId());
        reviewDTO.setUserNickname(userNickname);

        return reviewDTO;
    }

    private void checkExistingReview(long userId, long campId) {
        log.info("기존 리뷰 확인 - 유저ID: {}, 캠핑장ID: {}", userId, campId);

        if (reviewRepository.existsByUserIdAndCampId(userId, campId)) {
            log.warn("리뷰가 이미 존재함 - 유저ID: {}, 캠핑장ID: {}", userId, campId);
            throw new ReviewAlreadyExistsException("이미 이 캠핑장에 대한 리뷰를 작성하셨습니다.");
        }
    }

    private Review saveReview(ReviewDTO reviewDTO) {
        log.info("리뷰 저장 중 - 유저ID: {}, 캠핑장ID: {}", reviewDTO.getUserId(), reviewDTO.getCampId());

        Review review = Review.builder()
                .campId(reviewDTO.getCampId())
                .userId(reviewDTO.getUserId())
                .reviewContent(reviewDTO.getReviewContent())
                .rating(reviewDTO.getRating())
                .reviewCreatedDate(LocalDateTime.now())
                .reviewImageUrl(reviewDTO.getReviewImageUrl())
                .build();

        return reviewRepository.save(review);
    }

    private void updateMyReview(long userId, long reviewId, boolean isIncrement) {
        log.info("내 리뷰 업데이트 중 - 유저ID: {}, 리뷰ID: {}, 증가 여부: {}", userId, reviewId, isIncrement);

        myReviewRepository.findById(userId)
                .ifPresentOrElse(
                        myReview -> updateExistingMyReview(myReview, reviewId, isIncrement),
                        () -> createNewMyReview(userId, reviewId)
                );
    }

    private void updateExistingMyReview(MyReview myReview, long reviewId, boolean isIncrement) {
        log.info("기존 내 리뷰 업데이트 - 리뷰ID: {}, 증가 여부: {}", reviewId, isIncrement);

        if (isIncrement) {
            myReview.incrementReviewCnt(reviewId);
        } else {
            myReview.decrementReviewCnt(reviewId);
        }
        myReviewRepository.save(myReview);
    }

    private void createNewMyReview(long userId, long reviewId) {
        log.info("새로운 내 리뷰 생성 - 유저ID: {}, 리뷰ID: {}", userId, reviewId);

        MyReview newMyReview = new MyReview(userId);
        newMyReview.incrementReviewCnt(reviewId);
        myReviewRepository.save(newMyReview);
    }

    private void updateRating(long campId, int rating, boolean isIncrement) {
        log.info("캠핑장 평점 업데이트 중 - 캠핑장ID: {}, 평점: {}, 증가 여부: {}", campId, rating, isIncrement);

        if (isIncrement) {
            ratingSummaryRepository.incrementRating(campId, rating);
        } else {
            ratingSummaryRepository.decrementRating(campId, rating);
        }
    }

    private void adjustRating(int oldRating, int newRating, long campId) {

        updateRating(campId, oldRating, false);
        updateRating(campId, newRating, true);
    }

    private void incrementReviewCnt(long campId) {
        log.info("캠핑장 리뷰 카운트 증가 중 - 캠핑장ID: {}", campId);

        campingSummaryRepository.findById(campId)
                .ifPresentOrElse(
                        campingSummary -> {
                            campingSummary.incrementReviewCnt();
                            campingSummaryRepository.save(campingSummary);
                        },
                        () -> createNewCampingSummary(campId)
                );
    }

    private void createNewCampingSummary(long campId) {
        log.info("새로운 캠핑장 요약 생성 - 캠핑장ID: {}", campId);

        CampingSummary newSummary = CampingSummary.builder()
                .campId(campId)
                .bookmarkCnt(0)
                .reviewCnt(1)
                .build();
        campingSummaryRepository.save(newSummary);
    }

    private void decrementReviewCnt(long campId) {

        CampingSummary campingSummary = campingSummaryRepository.findById(campId)
                .orElseThrow(() -> new ReviewNotFoundException("해당 캠핑장의 리뷰 정보가 존재하지 않습니다."));

        campingSummary.decrementReviewCnt();
        campingSummaryRepository.save(campingSummary);
    }

    private Review findReviewById(long reviewId) {

        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewNotFoundException("리뷰를 찾을 수 없습니다."));
    }

    private Review updateReviewFromDTO(Review review, ReviewDTO reviewDTO) {

        return review.toBuilder()
                .reviewContent(reviewDTO.getReviewContent() != null ? reviewDTO.getReviewContent() : review.getReviewContent())
                .rating(reviewDTO.getRating())
                .reviewModificationDate(LocalDateTime.now())
                .reviewImageUrl(reviewDTO.getReviewImageUrl() != null ? reviewDTO.getReviewImageUrl() : review.getReviewImageUrl())
                .build();
    }

    private void verifyReviewPermission(long reviewerId, long userId, String action) {

        if (reviewerId != userId) {
            throw new NotAuthorizedException("이 리뷰를 " + action + "할 권한이 없습니다.");
        }
    }

}
