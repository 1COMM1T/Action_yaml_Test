package com.commit.campus.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@DynamoDbBean
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RatingSummary {
    private Long campId;
    private int totalRating;
    private int countRating;

    @DynamoDbPartitionKey
    @DynamoDbAttribute("camp_id")
    public Long getCampId() {
        return campId;
    }

    @DynamoDbAttribute("total_rating")
    public int getTotalRating() {
        return totalRating;
    }

    @DynamoDbAttribute("count_rating")
    public int getCountRating() {
        return countRating;
    }

}
