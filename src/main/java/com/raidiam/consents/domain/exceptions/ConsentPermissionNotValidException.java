package com.raidiam.consents.domain.exceptions;

public class ConsentPermissionNotValidException extends RuntimeException {
    public ConsentPermissionNotValidException(String message) {
        super(message);
    }
}
