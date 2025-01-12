package com.raidiam.consents.adapters.rest;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.raidiam.consents.adapters.rest.port.ConsentErrorResponse;
import com.raidiam.consents.domain.exceptions.ConsentNotFoundException;
import com.raidiam.consents.domain.exceptions.ConsentPermissionsWithDuplicateValueException;
import com.raidiam.consents.domain.exceptions.ConsentWithInvalidStatusException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.List;

import static com.raidiam.consents.domain.messages.ErrorMessage.*;

@RestControllerAdvice
public class ConsentControllerAdvice {

    private final Logger logger = LoggerFactory.getLogger(ConsentControllerAdvice.class);

    /**
     * Handle with consent not found
     *
     * @param ex                        Exception
     * @param request                   Request data
     * @return ConsentErrorResponse     Default error response
     */
    @ExceptionHandler(ConsentNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected ConsentErrorResponse handleConsentNotFound(RuntimeException ex, WebRequest request) {

        logger.error("Consent not found: {} {}", request, ex.getLocalizedMessage());

        return ConsentErrorResponse.builder()
                    .message(ex.getLocalizedMessage())
                    .build();
    }

    /**
     * Handle with invalid arguments
     *
     * @param ex                        Exception
     * @param request                   Request data
     * @return ConsentErrorResponse     Default error response
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ConsentErrorResponse handleMethodArgumentNotValid(MethodArgumentNotValidException ex, WebRequest request) {

        List<String> errors =
                ex.getBindingResult().getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();

        logger.error("Method argument not valid [1]: {} {}", request, errors);

        return ConsentErrorResponse.builder()
                    .message(INVALID_INPUT)
                    .errors(errors)
                    .build();
    }

    /**
     * Handle with invalid arguments
     *
     * @param ex                        Exception
     * @param request                   Request data
     * @return ConsentErrorResponse     Default error response
     */
    @ExceptionHandler(HandlerMethodValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ConsentErrorResponse handleMethodArgumentValidation(HandlerMethodValidationException ex, WebRequest request) {

        List<String> errors =
                ex.getParameterValidationResults()
                        .get(0)
                        .getResolvableErrors()
                        .stream()
                        .map(MessageSourceResolvable::getDefaultMessage)
                        .toList();

        logger.error("Method argument not valid [2]: {} {}", request, errors);

        return ConsentErrorResponse.builder()
                .message(INVALID_INPUT)
                .errors(errors)
                .build();
    }

    /**
     * Handle with unreadable message
     *
     * @param ex                        Exception
     * @param request                   Request data
     * @return ConsentErrorResponse     Default error response
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ConsentErrorResponse handleHttpMessageNotReadable(HttpMessageNotReadableException ex, WebRequest request) {

        if (ex.getCause() instanceof InvalidFormatException invalidFormatException) {
            if (invalidFormatException.getTargetType() != null && invalidFormatException.getTargetType().isEnum()) {
                var errors = String.format(INVALID_VALUE_IN_THE_LIST,
                        invalidFormatException.getPath().get(0).getFieldName(),
                        List.of(invalidFormatException.getTargetType().getEnumConstants()));

                logger.error("Http message not readable: {} {}", request, List.of(errors));

                return ConsentErrorResponse.builder()
                        .message(INVALID_INPUT)
                        .errors(List.of(errors))
                        .build();
            }
        }

        logger.error("Http message not readable: {} {}", request, ex.getLocalizedMessage());

        var error = ex.getLocalizedMessage().contains(REQUEST_BODY_MISSING) ? REQUEST_BODY_MISSING : ex.getLocalizedMessage();

        return ConsentErrorResponse.builder()
                .message(INVALID_INPUT)
                .errors(List.of(error))
                .build();
    }

    /**
     * Handle with invalid Enum value
     *
     * @param ex                        Exception
     * @param request                   Request data
     * @return ConsentErrorResponse     Default error response
     */
    @ExceptionHandler({ConsentPermissionsWithDuplicateValueException.class, ConsentWithInvalidStatusException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ConsentErrorResponse handlePermissionsAndStatusNotValid(RuntimeException ex, WebRequest request) {

        logger.error("Consent permissions/status not valid: {} {}", request, ex.getLocalizedMessage());

        return ConsentErrorResponse.builder()
                .message(INVALID_INPUT)
                .errors(List.of(ex.getLocalizedMessage()))
                .build();
    }

    /**
     * Handle with no resource found
     *
     * @param ex                        Exception
     * @param request                   Request data
     * @return ConsentErrorResponse     Default error response
     */
    @ExceptionHandler({NoResourceFoundException.class, NoHandlerFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected ConsentErrorResponse handleNoResourceFound(Exception ex, WebRequest request) {

        logger.error("No resource found: {}  {}", request, ex.getLocalizedMessage());

        return ConsentErrorResponse.builder()
                .message(ex.getLocalizedMessage())
                .build();
    }

    /**
     * Handle with Http method not supported
     *
     * @param ex                        Exception
     * @param request                   Request data
     * @return ConsentErrorResponse     Default error response
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    protected ConsentErrorResponse handleHttpRequestMethodNotSupported(Exception ex, WebRequest request) {

        logger.error("Http method not supported: {}  {}", request, ex.getLocalizedMessage());

        return ConsentErrorResponse.builder()
                .message(ex.getLocalizedMessage())
                .build();
    }

    /**
     * Default error handler
     *
     * @param ex                        Exception
     * @param request                   Request data
     * @return ConsentErrorResponse     Default error response
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected ConsentErrorResponse handleDefaultError(Exception ex, WebRequest request) {

        logger.error("Internal Server Error: {}  {}", request, ex.getLocalizedMessage());

        return ConsentErrorResponse.builder()
                .message(INTERNAL_SERVER_ERROR)
                .build();
    }
}
