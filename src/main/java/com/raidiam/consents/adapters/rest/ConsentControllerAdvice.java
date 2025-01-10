package com.raidiam.consents.adapters.rest;

import com.raidiam.consents.adapters.rest.port.ConsentErrorResponse;
import com.raidiam.consents.domain.exceptions.ConsentNotFoundException;
import com.raidiam.consents.domain.exceptions.ConsentPermissionNotValidException;
import com.raidiam.consents.domain.exceptions.ConsentStatusNotValidException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.List;

@RestControllerAdvice
public class ConsentControllerAdvice {

    @ExceptionHandler(
            value = {ConsentNotFoundException.class},
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected ConsentErrorResponse handleConsentNotFound(RuntimeException ex, WebRequest request) {
        return ConsentErrorResponse.builder()
                    .message(ex.getLocalizedMessage())
                    .build();
    }

    @ExceptionHandler(
            value = {MethodArgumentNotValidException.class},
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ConsentErrorResponse handleMethodArgumentNotValid(MethodArgumentNotValidException ex, WebRequest request) {
        List<String> errors =
                ex.getBindingResult().getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();

        return ConsentErrorResponse.builder()
                    .message("Invalid input")
                    .errors(errors)
                    .build();
    }

    @ExceptionHandler(
            value = {ConsentPermissionNotValidException.class, ConsentStatusNotValidException.class},
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ConsentErrorResponse handleConsentPermissionAndStatusNotValid(RuntimeException ex, WebRequest request) {
        return ConsentErrorResponse.builder()
                .message(ex.getLocalizedMessage())
                .build();
    }
}
