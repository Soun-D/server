package com.sound.sound.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(final Exception e) {
        final ErrorResponse response = new ErrorResponse(400, e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getErrorCode()));
    }

    @ExceptionHandler(SoundException.class)
    public ResponseEntity<ErrorResponse> SoundExceptionHandler(SoundException e) {
        ErrorResponse response = new ErrorResponse(e.getCode(), e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getErrorCode()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> BasicExceptionHandler(Exception e) {
        ErrorResponse response = new ErrorResponse(500, e.getMessage());
        e.printStackTrace();
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getErrorCode()));
    }
}
