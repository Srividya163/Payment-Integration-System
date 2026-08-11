package com.hulkhiretech.payments.pojo;

import java.util.List;

import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Schema(description = "Request payload to create a Stripe Checkout session")
public class CreatePaymentReq {
    
    @Schema(description = "URL to redirect the user after successful payment", example = "https://example.com/success")
    private String successUrl;

    @Schema(description = "URL to redirect the user if they cancel payment", example = "https://example.com/cancel")
    private String cancelUrl;
   
    @Schema(description = "List of items to include in the checkout session")
    List<LineItem> lineItems;

}
/*
*This class represents the data that client sends to your API when creating a payment.
*It defines the structure of incoming request data (like JSON from Postman or frontend). */
