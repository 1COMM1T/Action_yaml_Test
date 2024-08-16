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

        checkExistingReview(reviewDTO.getUserId(), reviewDTO.getCampId());

        Review savedReview = saveReview(reviewDTO);
        updateMyReview(savedReview.getUserId(), savedReview.getReviewId(), true);
        updateRating(savedReview.getCampId(), savedReview.getRating(), true);
        incrementReviewCnt(savedReview.getCampId());
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

        if (reviewRepository.existsByUserIdAndCampId(userId, campId)) {
            throw new ReviewAlreadyExistsException("이미 이 캠핑장에 대한 리뷰를 작성하셨습니다.");
        }
    }

    private Review saveReview(ReviewDTO reviewDTO) {

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

        myReviewRepository.findById(userId)
                .ifPresentOrElse(
                        myReview -> updateExistingMyReview(myReview, reviewId, isIncrement),
                        () -> createNewMyReview(userId, reviewId)
                );
    }

    private void updateExistingMyReview(MyReview myReview, long reviewId, boolean isIncrement) {

        if (isIncrement) {
            myReview.incrementReviewCnt(reviewId);
        } else {
            myReview.decrementReviewCnt(reviewId);
        }
        myReviewRepository.save(myReview);
    }

    private void createNewMyReview(long userId, long reviewId) {

        MyReview newMyReview = new MyReview(userId);
        newMyReview.incrementReviewCnt(reviewId);
        myReviewRepository.save(newMyReview);
    }

    private void updateRating(long campId, int rating, boolean isIncrement) {

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
