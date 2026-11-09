package com.hulkhiretech.payments.constant;

public enum ErrorCodeEnum {

    GENERIC_ERROR("30000", "An unexpected error occurred.Please try again later."),

    //Request-Level errors
    CREATE_PAYMENT_REQ_NULL("30001", "CreatePaymentreq is null"),
    SUCCESS_URL_MISSING("30002", "success Url is missing in createPaymentReq"),
    CANCEL_URL_MISSING("30003", "cancel Url is missing in createPaymentReq"),
    LINE_ITEMS_MISSING("30004", "line Items is missing in createPaymentReq"),

    //Line Item errors
    LINE_ITEM_NULL("30005", "lineItem is null"),
    CURRENCY_MISSING("30006", "currency is missing in lineItem"),
    PRODUCT_NAME_MISSING("30007", "productName is missing in lineItem"),
    UNIT_AMOUNT_INVALID("30008", "unitAmount must be greater than 0 for lineItem"),
    QUANTITY_INVALID("30009", "quantity must be greater than 0 for lineItem"),

    //Url format errors
    SUCCESS_URL_INVALID("30010", "successUrl is not a valid Http/Https URL"),
    CANCEL_URL_INVALID("30011", "cancelUrl is not a valid Http/Https URL"),

    ERROR_CONNECTING_TO_EXTERNAL_SERVICE("30012","Error connecting to external payment service"),
    STRIPE_API_ERROR("30013","<Dynamically prepare based on stripe error response>"),
    INVALID_STRIPE_RESPONSE("30014","Received invalid response from Stripe API"),
    MISSING_STRIPE_SIGNATURE_OR_EMPTY_REQUEST("30015","Missing Stripe signature or empty request payload"),
    STRIPE_SIGNATURE_VERIFICATION_ERROR("30016","Stripe signature verification failed");

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
