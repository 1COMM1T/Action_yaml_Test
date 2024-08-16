package com.commit.campus.view;

import lombok.Data;

@Data
public class ErrorView {
    private String errorType;
    private String message;

    public ErrorView(String errorCode, String message) {
        this.errorType = errorCode;
        this.message = message;
    }
}