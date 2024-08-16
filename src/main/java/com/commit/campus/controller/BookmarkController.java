package com.commit.campus.controller;

import com.commit.campus.common.CustomResolver;
import com.commit.campus.dto.BookmarkDTO;
import com.commit.campus.dto.BookmarkRequest;
import com.commit.campus.dto.BookmarkedCampingDTO;
import com.commit.campus.entity.User;
import com.commit.campus.service.BookmarkService;
import com.commit.campus.service.CampingService;
import com.commit.campus.view.BookmarkView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/bookmarks")
public class BookmarkController {

    private final BookmarkService bookmarkService;
    private final CampingService campingService;

    @Autowired
    public BookmarkController(BookmarkService bookmarkService, CampingService campingService) {
        this.bookmarkService = bookmarkService;
        this.campingService = campingService;
    }

    @PostMapping
    public ResponseEntity<Void> saveBookmark(@RequestBody BookmarkRequest bookmarkRequest, @CustomResolver User authenticationUser) {
        Long userId = authenticationUser.getUserId();

        bookmarkService.saveBookmark(bookmarkRequest, userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<BookmarkView>> getBookmarkByUserId(@CustomResolver User authenticationUser) {
        Long userId = authenticationUser.getUserId();

        List<BookmarkDTO> sortedBookmarkDTOs = bookmarkService.getBookmark(userId).stream()
                .sorted((b1, b2) -> b2.getCreatedBookmarkDate().compareTo(b1.getCreatedBookmarkDate()))
                .collect(Collectors.toList());

        List<BookmarkView> bookmarkViews = sortedBookmarkDTOs.stream()
                .map(bookmarkDTO -> {
                    BookmarkedCampingDTO campingData = campingService.getBookmarkedCamping(bookmarkDTO.getCampId());
                    return BookmarkView.builder()
                            .campName(Optional.ofNullable(campingData.getCampName()).orElse(""))
                            .doName(campingData.getDoName())
                            .sigunguName(campingData.getSigunguName())
                            .postCode(campingData.getPostCode())
                            .induty(Optional.ofNullable(campingData.getInduty()).orElse(""))
                            .firstImageUrl(Optional.ofNullable(campingData.getFirstImageUrl()).orElse(""))
                            .build();
                })
                .collect(Collectors.toList());


        return ResponseEntity.status(HttpStatus.OK).body(bookmarkViews);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteBookmarkByUserId(@CustomResolver User authenticationUser, @RequestParam Long campId) {
        Long userId = authenticationUser.getUserId();

        bookmarkService.deleteBookmark(userId, campId);
        return ResponseEntity.ok().build();
    }

}
