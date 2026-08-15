package com.hulkhiretech.payments.constant;

public enum ErrorCodeEnum {

    SUCCESS_URL_BLANK("10001", "Success URL must not be blank"),
    SUCCESS_URL_INVALID("10002","Success URL must be valid HTTP/HTTPS URL"),

    CANCEL_URL_BLANK("10003","Cancel URL must not be blank"),
    CANCEL_URL_INVALID("10004","Cancel URL must be a valid URL"),
            
    LINE_ITEMS_EMPTY("10005","Line items cannot be empty"),

    CURRENCY_BLANK("10006","Currency must not be blank"),
    CURRENCY_INVALID("10007","Currency must be 3-letter ISO currency code"),

    PRODUCT_NAME_BLANK("10008","Product name must not be blank"),
    PRODUCT_NAME_TOO_LONG("10009","Product name must not exceed 100 characters"),

    UNIT_AMOUNT_REQUIRED("10010","Unit amount is required"),
    UNIT_AMOUNT_INVALID("10011", "Unit amount must be greater than 0"),

    QUANTITY_REQUIRED("10012","Quantity must not be null"),
    QUANTITY_INVALID("10013","Quantity must be greater than 0");

    private final String errorCode;
    private final String errorMessage;

    ErrorCodeEnum(String errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
