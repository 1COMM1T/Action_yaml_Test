package com.commit.campus.advice;

import com.commit.campus.common.exceptions.ErrorType;
import com.commit.campus.common.exceptions.NotAuthorizedException;
import com.commit.campus.common.exceptions.ReviewAlreadyExistsException;
import com.commit.campus.common.exceptions.ReviewNotFoundException;
import com.commit.campus.view.ErrorView;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionAdvice {

    @ExceptionHandler(ReviewAlreadyExistsException.class)
    public ResponseEntity<ErrorView> handleReviewAlreadyExists(ReviewAlreadyExistsException ex) {

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ErrorView(ErrorType.REVIEW_ALREADY_EXISTS, ex.getMessage()));
    }

    @ExceptionHandler(NotAuthorizedException.class)
    public ResponseEntity<ErrorView> handleNotAuthorizedException(NotAuthorizedException ex) {

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(new ErrorView(ErrorType.NOT_AUTHORIZED, ex.getMessage()));
    }

    @ExceptionHandler(ReviewNotFoundException.class)
    public ResponseEntity<ErrorView> handleReviewNotFoundException(ReviewNotFoundException exception) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorView(ErrorType.REVIEW_NOT_FOUND, exception.getMessage()));
    }

}
