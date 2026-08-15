package com.hulkhiretech.payments.pojo;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LineItems {

    @NotNull(message = "CURRENCY_BLANK")
    private String currency;

    @NotNull(message = "PRODUCT_NAME_BLANK")
    @Size(max = 100, message = "PRODUCT_NAME_TOO_LONG")
    private String productName;

    @NotNull(message = "UNIT_AMOUNT_REQUIRED")
    @Positive(message = "UNIT_AMOUNT_INVALID")
    private Integer unitAmount;

    @NotNull(message = "QUANTITY_REQUIRED")
    @Positive(message = "QUANTITY_INVALID")
    private Integer quantity;
}