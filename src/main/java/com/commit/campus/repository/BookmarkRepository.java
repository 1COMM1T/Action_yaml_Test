package com.commit.campus.repository;


import com.commit.campus.entity.Bookmark;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;

import java.util.List;
import java.util.stream.Collectors;


@Repository
public class BookmarkRepository {

    private final DynamoDbTable<Bookmark> bookmarkDynamoDBTable;

    public BookmarkRepository(DynamoDbEnhancedClient dynamoDbEnhancedClient) {
        this.bookmarkDynamoDBTable = dynamoDbEnhancedClient.table("Bookmark", TableSchema.fromBean(Bookmark.class));
    }

    public void save(Bookmark bookmark) {
        bookmarkDynamoDBTable.putItem(bookmark);
    }


    public List<Bookmark> getBookmark(Long userId) {
        Key key = Key.builder()
                .partitionValue(userId)
                .build();

        return bookmarkDynamoDBTable.query(r -> r.queryConditional(QueryConditional.keyEqualTo(key)))
                .items()
                .stream()
                .collect(Collectors.toList());
    }

    public void delete(Long userId, Long campId) {
        Key key = Key.builder()
                .partitionValue(userId)
                .sortValue(campId)
                .build();
        bookmarkDynamoDBTable.deleteItem(key);
    }
}
