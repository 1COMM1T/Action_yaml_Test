package com.commit.campus.service.impl;

import com.commit.campus.dto.BookmarkDTO;
import com.commit.campus.dto.BookmarkRequest;
import com.commit.campus.entity.Bookmark;
import com.commit.campus.entity.CampingSummary;
import com.commit.campus.repository.BookmarkRepository;
import com.commit.campus.repository.CampingSummaryRepository;
import com.commit.campus.service.BookmarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookmarkServiceImpl implements BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final CampingSummaryRepository campingSummaryRepository;

    @Autowired
    public BookmarkServiceImpl(BookmarkRepository bookmarkRepository, CampingSummaryRepository campingSummaryRepository) {
        this.bookmarkRepository = bookmarkRepository;
        this.campingSummaryRepository = campingSummaryRepository;
    }

    @Override
    @Transactional
    public void saveBookmark(BookmarkRequest bookmarkRequest, Long userId) {
        LocalDateTime currentTime = LocalDateTime.now();

        Bookmark bookmark = new Bookmark();
        bookmark.setUserId(userId);
        bookmark.setCampId(bookmarkRequest.getCampId());
        bookmark.setCreatedBookmarkDate(currentTime);
        bookmarkRepository.save(bookmark);

        Optional<CampingSummary> optionalCampingSummary = campingSummaryRepository.findById(bookmarkRequest.getCampId());
        if (optionalCampingSummary.isPresent()) {
            // CampingSummary가 존재할 경우 업데이트
            CampingSummary campingSummary = optionalCampingSummary.get();
            CampingSummary updateCampingSummary = CampingSummary.builder()
                    .campId(campingSummary.getCampId())
                    .bookmarkCnt(campingSummary.getBookmarkCnt() + 1)
                    .reviewCnt(campingSummary.getReviewCnt())
                    .build();
            campingSummaryRepository.save(updateCampingSummary);
        } else {
            // CampingSummary가 존재하지 않을 경우 새로 생성
            CampingSummary newCampingSummary = CampingSummary.builder()
                    .campId(bookmarkRequest.getCampId())
                    .bookmarkCnt(1)
                    .reviewCnt(0)
                    .build();
            campingSummaryRepository.save(newCampingSummary);
        }
    }

    @Override
    public List<BookmarkDTO> getBookmark(Long userId) {
        List<Bookmark> bookmarks = bookmarkRepository.getBookmark(userId);
        List<BookmarkDTO> bookmarkDTOS = bookmarks.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return bookmarkDTOS;
    }

    @Override
    public void deleteBookmark(Long userId, Long campId) {
        CampingSummary previousCampingSummary = campingSummaryRepository.findById(campId).orElseThrow(IllegalArgumentException::new);

        bookmarkRepository.delete(userId, campId);

        CampingSummary updateCampingSummary = CampingSummary.builder()
                .campId(previousCampingSummary.getCampId())
                .bookmarkCnt(previousCampingSummary.getBookmarkCnt()-1)
                .build();

        campingSummaryRepository.save(updateCampingSummary);

    }

    private BookmarkDTO convertToDTO(Bookmark bookmark) {
        return BookmarkDTO.builder()
                .userId(bookmark.getUserId())
                .campId(bookmark.getCampId())
                .createdBookmarkDate(bookmark.getCreatedBookmarkDate())
                .build();
    }
}
