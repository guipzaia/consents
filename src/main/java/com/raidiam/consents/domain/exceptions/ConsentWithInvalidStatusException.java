package com.raidiam.consents.domain.exceptions;

public class ConsentWithInvalidStatusException extends RuntimeException {
    public ConsentWithInvalidStatusException(String message) {
        super(message);
    }
}
