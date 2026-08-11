package com.hulkhiretech.payments.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.hulkhiretech.payments.constant.ErrorCodeEnum;

import com.hulkhiretech.payments.pojo.ErrorResponse;

import lombok.extern.slf4j.Slf4j;

/**
 * Global exception handler for handling application-wide exceptions.
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(StripeProviderException.class)
    public ResponseEntity<ErrorResponse> handleStripeProviderException(StripeProviderException ex) {
        log.error("StripeProviderException: {}", ex.toString());
        
        HttpStatus status=ex.getHttpStatus() ;
        
        ErrorResponse body = new ErrorResponse();
        body.setErrorcode(ex.getErrorCode());
        body.setErrorMessage(ex.getErrorMessage() != null ? ex.getErrorMessage() : ex.getMessage());

        log.info("Returning error response with status: {}, body: {}", status, body);
        return new ResponseEntity<>(body, status);
    }
    /**
     * Handle Exception.class, return ErrorResponse with Generic error code and message and Http 500
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        log.error("Generic Exception: {}", ex.toString());

        HttpStatus status=HttpStatus.INTERNAL_SERVER_ERROR;

        ErrorResponse body = new ErrorResponse();
        body.setErrorcode(ErrorCodeEnum.GENERIC_ERROR.getErrorCode());
        body.setErrorMessage(ErrorCodeEnum.GENERIC_ERROR.getErrorMessage());

        log.info("Returning generic error response with status: {}, body: {}", status, body);
        return new ResponseEntity<>(body, status);
    }
}
