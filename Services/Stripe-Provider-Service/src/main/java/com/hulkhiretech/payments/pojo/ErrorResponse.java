package com.hulkhiretech.payments.pojo;

import lombok.Data;

@Data
public class ErrorResponse {
    private String errorcode;
    private String errorMessage;
}
/* This class defines how your API error response will look. */