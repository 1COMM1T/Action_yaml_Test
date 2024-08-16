package com.commit.campus.entity;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

import java.time.LocalDateTime;

@DynamoDbBean
public class Bookmark {

    private Long userId;
    private Long campId;
    private LocalDateTime createdBookmarkDate;

    @DynamoDbPartitionKey
    @DynamoDbAttribute("user_id")
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @DynamoDbSortKey
    @DynamoDbAttribute("camp_id")
    public Long getCampId() {
        return campId;
    }

    public void setCampId(Long campId) {
        this.campId = campId;
    }


    @DynamoDbAttribute("created_bookmark_date")
    public LocalDateTime getCreatedBookmarkDate() {
        return createdBookmarkDate;
    }

    public void setCreatedBookmarkDate(LocalDateTime createdBookmarkDate) {
        this.createdBookmarkDate = createdBookmarkDate;
    }
}
