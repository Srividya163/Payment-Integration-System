package com.hulkhiretech.payments.service.impl;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.hulkhiretech.payments.service.helper.CreatePaymentHelper;
import com.hulkhiretech.payments.service.ValidationService;
import com.hulkhiretech.payments.http.HttpServiceEngine;
import com.hulkhiretech.payments.http.HttpRequest;
import com.hulkhiretech.payments.service.interfaces.PaymentService;
import com.hulkhiretech.payments.pojo.CreatePaymentReq;
import com.hulkhiretech.payments.pojo.PaymentResponse;
import com.hulkhiretech.payments.stripe.CheckoutSessionResponse;
import com.hulkhiretech.payments.utl.JsonUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;



@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    /**
     * Make Rest API call to stripe create-session API
     * Prepare the request to call stripe
     * url,post,formencoded request body,basic auth header
     * what spring boot library is available to make rest api call? RestTemplate, WebClient
     * How to use RestController to make API call to  stripe
     * Response handling.Both success and failure 
     */
    private final HttpServiceEngine httpServiceEngine;

    private final CreatePaymentHelper createPaymentHelper;

    private final JsonUtil jsonUtil;

    private final ValidationService validationService;

    @Override
    public PaymentResponse createPayment(CreatePaymentReq createPaymentReq) {
        
        // Implement the logic to create a payment here
        log.info("Processing payment creation logic for request: {}", createPaymentReq);

        /*
        * Creates a stripe payment session and returns a mapped payment response
        *
        * Flow:
        * Logs and Validates the incoming createPaymentReq
        * Prepares Stripe session creation Http request
        * Executes the Http call using HttpServiceEngine.
        * Processes Stripe respose into CheckoutSessionResponse
        * Maps the CheckoutSessionResponse to our internal PaymentResponse object.
        * Returns the final Response
        */

        validationService.isValid(createPaymentReq);

        HttpRequest httpRequest = createPaymentHelper.prepareStripeCreateSessionRequest(createPaymentReq);

        ResponseEntity<String>httpResponse = httpServiceEngine.makeHttpCall(httpRequest);
        log.info("Received response from HttpServiceEngine: {}", httpResponse);

        CheckoutSessionResponse checkoutSession = createPaymentHelper.processStriResponse(httpResponse);
        log.info("Processed Stripe response and obtainedCheckoutSessionResponse:{}",checkoutSession);

        PaymentResponse paymentResponse = mapCheckoutSessionToPaymentResponse(checkoutSession);
        log.info("Mapped PaymentResponse: {}", paymentResponse);

        return paymentResponse;
    }

    
    /**Write a map method to take CheckoutSessionResponse
     * and convert it to PaymentResponse which is
     * our internal response object. This way we are not
     */
    public PaymentResponse mapCheckoutSessionToPaymentResponse(CheckoutSessionResponse checkoutSession) {
        if (checkoutSession == null) {
            log.warn("mapCheckoutSessionToPaymentResponse: Received null CheckoutSessionResponse"); 
            return null;
        }
        PaymentResponse paymentResponse = new PaymentResponse();
        paymentResponse.setStripeSessionId(checkoutSession.getId());
        paymentResponse.setHostedPageUrl(checkoutSession.getUrl());

        log.info("Mapped CheckoutSessionResponse to PaymentResponse: {}", paymentResponse);
        return paymentResponse;
    }

}
