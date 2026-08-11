package com.hulkhiretech.payments.stripe;
import lombok.Data;

@Data
public class StripeError {
    private String type;
    private String message;
    private String code;
    private String param;
}
