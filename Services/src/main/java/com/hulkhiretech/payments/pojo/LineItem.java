package com.hulkhiretech.payments.pojo;

import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Schema(description = "Single line item for a checkout session")
public class LineItem {
    @Schema(description = "ISO currency code for the Item(lowercase preferred:usd)", example = "usd")
    private String currency;

    @Schema(description = "Human readable product name is displayed on chackout page", example = "T-shirt (Blue)")
    private String productName;

    @Schema(description = "Unit amount in the smallest currency unit (e.g., cents) must be greater than 0.", example = "1990")
    private int unitAmount;

    @Schema(description = "Quantity of this item to charge.Must be >0", example = "1")
    private int quantity;

}
/*This class represents a single item in a payment request, 
containing product details like name, price, currency, and quantity. */
