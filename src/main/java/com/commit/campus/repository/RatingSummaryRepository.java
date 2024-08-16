package com.commit.campus.repository;

import com.commit.campus.entity.RatingSummary;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

@Repository
@Slf4j
public class RatingSummaryRepository {

    private final DynamoDbTable<RatingSummary> ratingSummaryTable;

    public RatingSummaryRepository(DynamoDbEnhancedClient dynamoDbEnhancedClient) {
        this.ratingSummaryTable = dynamoDbEnhancedClient.table("RATING_SUMMARY", TableSchema.fromBean(RatingSummary.class));
    }

    public void incrementRating(Long campId, int rating) {
        updateRatingSummary(campId, rating, true);
    }

    public void decrementRating(Long campId, int rating) {
        updateRatingSummary(campId, rating, false);
    }

    private void updateRatingSummary(Long campId, int rating, boolean isAdding) {
        Key key = Key.builder().partitionValue(campId).build();
        RatingSummary ratingSummary = ratingSummaryTable.getItem(key);

        if (ratingSummary == null) {
            ratingSummary = new RatingSummary();
            ratingSummary.setCampId(campId);
            ratingSummary.setTotalRating(0);
            ratingSummary.setCountRating(0);
        }

        if (isAdding) {
            ratingSummary.setTotalRating(ratingSummary.getTotalRating() + rating);
            ratingSummary.setCountRating(ratingSummary.getCountRating() + 1);
        } else {
            ratingSummary.setTotalRating(ratingSummary.getTotalRating() - rating);
            ratingSummary.setCountRating(ratingSummary.getCountRating() - 1);
        }

        ratingSummaryTable.putItem(ratingSummary);
    }
}
