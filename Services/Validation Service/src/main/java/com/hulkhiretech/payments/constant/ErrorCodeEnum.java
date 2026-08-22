package com.hulkhiretech.payments.constant;

public enum ErrorCodeEnum {

    GENERIC_ERROR("10000","An unexpected error occurred"),

    // ============================================================
    // 1. PAYMENT REQUEST - ROOT OBJECT VALIDATION
    // ============================================================

    USER_NULL("10001","User must not be null"),
    PAYMENT_NULL("10002","Payment must not be null"),

    // ============================================================
    // 2. USER VALIDATION
    // ============================================================

    END_USER_ID_BLANK("10003","End user ID must not be blank"),
    END_USER_ID_TOO_LONG("10004","End user ID must not exceed 50 characters"),
    FIRSTNAME_BLANK("10005","First name must not be blank"),
    FIRSTNAME_TOO_LONG("10006","First name must not exceed 50 characters"),

    LASTNAME_BLANK(
            "10007",
            "Last name must not be blank"
    ),

    LASTNAME_TOO_LONG(
            "10008",
            "Last name must not exceed 50 characters"
    ),

    EMAIL_BLANK(
            "10009",
            "Email must not be blank"
    ),

    EMAIL_INVALID(
            "10010",
            "Email must be valid"
    ),

    MOBILE_PHONE_BLANK(
            "10011",
            "Mobile phone must not be blank"
    ),

    MOBILE_PHONE_INVALID(
            "10012",
            "Mobile phone must be valid"
    ),


    // ============================================================
    // 3. PAYMENT VALIDATION
    // ============================================================

    PAYMENT_CURRENCY_BLANK(
            "10013",
            "Payment currency must not be blank"
    ),

    PAYMENT_CURRENCY_INVALID(
            "10014",
            "Payment currency must be a 3-letter ISO currency code"
    ),

    AMOUNT_REQUIRED(
            "10015",
            "Amount is required"
    ),

    AMOUNT_INVALID(
            "10016",
            "Amount must be greater than 0"
    ),

    BRAND_NAME_BLANK(
            "10017",
            "Brand name must not be blank"
    ),

    BRAND_NAME_TOO_LONG(
            "10018",
            "Brand name must not exceed 100 characters"
    ),

    LOCALE_BLANK(
            "10019",
            "Locale must not be blank"
    ),

    LOCALE_INVALID(
            "10020",
            "Locale must be valid"
    ),

    COUNTRY_BLANK(
            "10021",
            "Country must not be blank"
    ),

    COUNTRY_INVALID(
            "10022",
            "Country must be a 2-letter ISO country code"
    ),

    MERCHANT_TXN_REF_BLANK(
            "10023",
            "Merchant transaction reference must not be blank"
    ),

    MERCHANT_TXN_REF_TOO_LONG(
            "10024",
            "Merchant transaction reference must not exceed 100 characters"
    ),

    PAYMENT_METHOD_BLANK(
            "10025",
            "Payment method must not be blank"
    ),

    PROVIDER_BLANK(
            "10026",
            "Provider must not be blank"
    ),

    PAYMENT_TYPE_BLANK(
            "10027",
            "Payment type must not be blank"
    ),

    SUCCESS_URL_BLANK(
            "10028",
            "Success URL must not be blank"
    ),

    SUCCESS_URL_INVALID(
            "10029",
            "Success URL must be a valid HTTP/HTTPS URL"
    ),

    CANCEL_URL_BLANK(
            "10030",
            "Cancel URL must not be blank"
    ),

    CANCEL_URL_INVALID(
            "10031",
            "Cancel URL must be a valid HTTP/HTTPS URL"
    ),

    LINE_ITEMS_EMPTY(
            "10032",
            "Line items cannot be empty"
    ),


    // ============================================================
    // 4. LINE ITEMS VALIDATION
    // ============================================================

    LINE_ITEM_CURRENCY_BLANK("10033","Line item currency must not be blank"),
    LINE_ITEM_CURRENCY_INVALID("10034","Line item currency must be a 3-letter ISO currency code"),
    PRODUCT_NAME_BLANK("10035","Product name must not be blank"),
    PRODUCT_NAME_TOO_LONG("10036","Product name must not exceed 100 characters"),
    UNIT_AMOUNT_REQUIRED("10037","Unit amount is required"),
    UNIT_AMOUNT_INVALID("10038","Unit amount must be greater than 0"),
    QUANTITY_REQUIRED("10039","Quantity must not be null"),
    QUANTITY_INVALID("10040","Quantity must be greater than 0"),
    FIRSTNAME_CONTAINS_HELLO("10041","First name must not contain the word 'hello'"),
    LASTNAME_CONTAINS_HELLO("10042","Last name must not contain the word 'hello'"),
    DUPLICATE_TRANSACTION("10043","Duplicate transaction detected"),
    FAILED_TO_SAVE_PAYMENT_REQUEST("10044","Failed to save payment request due to an unexpected error");

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