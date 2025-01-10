package com.raidiam.consents.domain.exceptions;

public class ConsentStatusNotValidException extends RuntimeException {
    public ConsentStatusNotValidException(String message) {
        super(message);
    }
}
