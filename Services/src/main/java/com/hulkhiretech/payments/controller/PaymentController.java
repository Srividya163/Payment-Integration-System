package com.hulkhiretech.payments.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.hulkhiretech.payments.pojo.CreatePaymentReq;
import com.hulkhiretech.payments.pojo.PaymentResponse;
import com.hulkhiretech.payments.service.interfaces.PaymentService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/v1/payments")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Payments", description = "Endpoints to create and manage payments")
public class PaymentController {

    private final PaymentService paymentService;
    @PostMapping
    @Operation(summary = "Create payment", description = "Creates a Stripe Checkout Session and returns session id and a redirect URL to the hosted checkout page")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Payment session created",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PaymentResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid request"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public PaymentResponse paymentResponse(@RequestBody CreatePaymentReq createPaymentReq){
        log.info("Creating payment request...createPaymentReq:{}",createPaymentReq);

        PaymentResponse paymentResponse = paymentService.createPayment(createPaymentReq);
        log.info("Payment created with response: {}", paymentResponse);

        return paymentResponse;
    }
}
