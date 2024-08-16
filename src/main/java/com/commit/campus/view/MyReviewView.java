package com.commit.campus.view;

import lombok.Data;

@Data
public class MyReviewView {
    private long reviewId;
    private long campId;
    private String reviewContent;
    private int rating;
}
