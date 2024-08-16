package com.commit.campus.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class BookmarkDTO {

    private Long userId;
    private Long campId;
    private LocalDateTime createdBookmarkDate;

    @Builder
    public BookmarkDTO(Long userId, Long campId, LocalDateTime createdBookmarkDate) {
        this.userId = userId;
        this.campId = campId;
        this.createdBookmarkDate = createdBookmarkDate;
    }
}
