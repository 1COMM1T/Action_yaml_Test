package com.commit.campus.service;

import com.commit.campus.common.exceptions.ReviewNotFoundException;
import com.commit.campus.dto.CampingDTO;
import com.commit.campus.dto.ReviewDTO;
import com.commit.campus.entity.Camping;
import com.commit.campus.entity.MyReview;
import com.commit.campus.entity.Review;
import com.commit.campus.repository.CampingRepository;
import com.commit.campus.repository.MyReviewRepository;
import com.commit.campus.repository.ReviewRepository;
import com.commit.campus.service.impl.MyReviewServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MyReviewServiceTests {

    @Mock
    private MyReviewRepository myReviewRepository;
    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private CampingRepository campingRepository;
    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private MyReviewServiceImpl myReviewService;

    private MyReview myReview;
    private Review review1, review2;
    private Camping camping1, camping2;
    private ReviewDTO reviewDTO1, reviewDTO2;
    private CampingDTO campingDTO1, campingDTO2;

    @BeforeEach
    void setUp() {
        LocalDateTime now = LocalDateTime.now();

        myReview = MyReview.builder()
                .userId(1L)
                .reviewCount(2)
                .reviewIds(Arrays.asList(1L, 2L))
                .lastReviewDate(now)
                .createdAt(now.minusDays(1))
                .updatedAt(now)
                .build();

        review1 = Review.builder()
                .reviewId(1L)
                .campId(1L)
                .userId(1L)
                .reviewContent("좋은 캠핑장이에요")
                .rating(5)
                .reviewCreatedDate(now.minusDays(1))
                .reviewModificationDate(now.minusDays(1))
                .reviewImageUrl("image1.jpg")
                .build();

        review2 = Review.builder()
                .reviewId(2L)
                .campId(2L)
                .userId(1L)
                .reviewContent("괜찮은 캠핑장이에요")
                .rating(4)
                .reviewCreatedDate(now)
                .reviewModificationDate(now)
                .reviewImageUrl("image2.jpg")
                .build();

        camping1 = new Camping();
        camping1.setCampId(1L);

        camping2 = new Camping();
        camping2.setCampId(2L);

        reviewDTO1 = new ReviewDTO();
        reviewDTO2 = new ReviewDTO();

        campingDTO1 = new CampingDTO();
        campingDTO2 = new CampingDTO();
    }

    @Test
    void 내_리뷰_조회_성공(){
        long userId = 1L;
        Pageable pageable = PageRequest.of(0, 10);
        List<Review> reviews = Arrays.asList(review1, review2);
        Page<Review> reviewPage = new PageImpl<>(reviews, pageable, reviews.size());

        when(myReviewRepository.findById(userId)).thenReturn(Optional.of(myReview));
        when(reviewRepository.findByReviewIdIn(myReview.getReviewIds(), pageable)).thenReturn(reviewPage);
        when(modelMapper.map(review1, ReviewDTO.class)).thenReturn(reviewDTO1);
        when(modelMapper.map(review2, ReviewDTO.class)).thenReturn(reviewDTO2);

        Page<ReviewDTO> result = myReviewService.getMyReviews(userId, pageable);

        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        verify(myReviewRepository).findById(userId);
        verify(reviewRepository).findByReviewIdIn(myReview.getReviewIds(), pageable);
        verify(modelMapper, times(2)).map(any(Review.class), eq(ReviewDTO.class));
    }

    @Test
    void 내_리뷰_조회_실패_리뷰없음() {
        long userId = 1L;
        Pageable pageable = PageRequest.of(0, 10);

        when(myReviewRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(ReviewNotFoundException.class, () -> myReviewService.getMyReviews(userId, pageable));
        verify(myReviewRepository).findById(userId);
        verify(reviewRepository, never()).findByReviewIdIn(any(), any());
    }

    @Test
    void 리뷰한_캠핑장_조회_성공() {
        long userId = 1L;
        Pageable pageable = PageRequest.of(0, 10);
        List<Review> reviews = Arrays.asList(review1, review2);
        List<Camping> campings = Arrays.asList(camping1, camping2);
        Page<Camping> campingPage = new PageImpl<>(campings, pageable, campings.size());

        when(myReviewRepository.findById(userId)).thenReturn(Optional.of(myReview));
        when(reviewRepository.findByReviewIdIn(myReview.getReviewIds())).thenReturn(reviews);
        when(campingRepository.findByCampIdIn(Arrays.asList(1L, 2L), pageable)).thenReturn(campingPage);
        when(modelMapper.map(camping1, CampingDTO.class)).thenReturn(campingDTO1);
        when(modelMapper.map(camping2, CampingDTO.class)).thenReturn(campingDTO2);

        Page<CampingDTO> result = myReviewService.getReviewedCampings(userId, pageable);

        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        verify(myReviewRepository).findById(userId);
        verify(reviewRepository).findByReviewIdIn(myReview.getReviewIds());
        verify(campingRepository).findByCampIdIn(Arrays.asList(1L, 2L), pageable);
        verify(modelMapper, times(2)).map(any(Camping.class), eq(CampingDTO.class));
    }

    @Test
    void 리뷰한_캠핑장_조회_실패_리뷰없음() {
        long userId = 1L;
        Pageable pageable = PageRequest.of(0, 10);

        when(myReviewRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(ReviewNotFoundException.class, () -> myReviewService.getReviewedCampings(userId, pageable));
        verify(myReviewRepository).findById(userId);
        verify(reviewRepository, never()).findByReviewIdIn(any());
        verify(campingRepository, never()).findByCampIdIn(any(), any());
    }
}
