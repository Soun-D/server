package com.sound.sound.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(SoundException.class)
    public ErrorResponse SoundExceptionHandler(SoundException e) {
        return new ErrorResponse(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ErrorResponse BasicExceptionHandler(Exception e) {
        return new ErrorResponse(500, e.getMessage());
    }
}
