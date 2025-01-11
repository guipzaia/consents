package com.raidiam.consents.domain.exceptions;

public class ConsentPermissionsWithDuplicateValueException extends RuntimeException {
    public ConsentPermissionsWithDuplicateValueException(String message) {
        super(message);
    }
}
