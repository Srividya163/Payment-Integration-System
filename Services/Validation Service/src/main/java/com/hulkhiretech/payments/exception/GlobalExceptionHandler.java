package com.hulkhiretech.payments.exception;
        
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.hulkhiretech.payments.constant.ErrorCodeEnum;
import com.hulkhiretech.payments.pojo.ErrorResponse;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidException(
            MethodArgumentNotValidException ex) {
                log.error("Validation error occured: {}", ex.getMessage());

        FieldError fieldError = ex.getBindingResult()
                .getFieldErrors()
                .get(0);

        String enumKey =fieldError.getDefaultMessage();
        
        ErrorCodeEnum errorCodeEnum =ErrorCodeEnum.valueOf(enumKey);

        ErrorResponse response=new ErrorResponse(
                            errorCodeEnum.getErrorCode(),
                            errorCodeEnum.getErrorMessage()
                    );

        log.error("Validation errors: {}", response);

        return ResponseEntity.badRequest().body(response);
    }
}
