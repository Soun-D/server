package com.sound.sound.exception;

import lombok.Getter;

@Getter
public class SoundException extends RuntimeException {

    private final Integer code;
    private final String message;

    public SoundException(Integer code, String message) {
        super(message);
        this.message = message;
        this.code = code;
    }
}
