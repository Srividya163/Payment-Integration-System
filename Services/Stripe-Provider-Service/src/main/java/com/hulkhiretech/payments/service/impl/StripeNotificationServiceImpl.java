package com.hulkhiretech.payments.service.impl;

import org.springframework.stereotype.Service;
import com.hulkhiretech.payments.constant.ErrorCodeEnum;
import com.hulkhiretech.payments.exception.StripeProviderException;
import com.hulkhiretech.payments.service.StripeNotificationService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.net.Webhook;
import com.hulkhiretech.payments.stripe.CheckoutSessionResponse;
import com.hulkhiretech.payments.utl.JsonUtil;

import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor 
public class StripeNotificationServiceImpl implements StripeNotificationService {

    @Value("${stripe.webhook.endpoint-secret}")
    private String endpointSecret;

    List<String> successEvents = List.of(
        "checkout.session.completed",
        "checkout.session.async_payment_failed"
    );

    List<String> failedEvents = List.of(
        "checkout.session.async_payment_succeeded"
    );

    private final JsonUtil jsonUtil;

    @Override
    public void processNotification(String stripeSignature,String jsonRequest) {
        log.info("StripeNotificationServiceImpl.processNotification"+"called with payload length:{}",
                jsonRequest != null ? jsonRequest.length() : 0);

        log.info("Stripe-signature header: {}", stripeSignature);

        //if signature is null or empty, or jsonRequest is null or empty, log and then throw stripe provider exception
        if ((stripeSignature == null || stripeSignature.isEmpty()) || (jsonRequest == null || jsonRequest.isEmpty())){
            log.error("Invalid Stripe signature or notification payload");
            throw new StripeProviderException(
                ErrorCodeEnum.MISSING_STRIPE_SIGNATURE_OR_EMPTY_REQUEST.getErrorCode(),
                ErrorCodeEnum.MISSING_STRIPE_SIGNATURE_OR_EMPTY_REQUEST.getErrorMessage(),
                HttpStatus.BAD_REQUEST);
        }

        Event event = isSignatureValid(stripeSignature, jsonRequest);

        log.info("Stripe signature valid for eventType id= {}, type={}", 
        event.getId(), event.getType());
    
        //if the event doesn't belong to success or failure
        if(!successEvents.contains(event.getType()) && !failedEvents.contains(event.getType())) {
            log.warn("Received Stripe event of type{} which is not in success or failure  events list.Ignoring.", event.getType());
            return;
        }

        if(successEvents.contains(event.getType())) {
            log.info("Received Stripe event of type {} which is in success events list. Processing as success.", event.getType());
            //process the success event
            String notificationJson =event.getDataObjectDeserializer().getRawJson();
            log.info("Succes event :{}", event);
            log.info("success event raw JSON: notificationJson:{}", notificationJson);

            CheckoutSessionResponse response = jsonUtil.convertJsonToObject(notificationJson, CheckoutSessionResponse.class);

            if(response != null && "paid".equalsIgnoreCase(response.getPaymentStatus())){
                triggerSuccessPayment(response); 
                return;
            }

            log.error("failed to parse Stripe event data into CheckOutSessionResponse. notification: {}", notificationJson);

            throw new StripeProviderException(
                ErrorCodeEnum.INVALID_STRIPE_RESPONSE.getErrorCode(),
                ErrorCodeEnum.INVALID_STRIPE_RESPONSE.getErrorMessage(),
                HttpStatus.BAD_REQUEST
            );
        } 
        if(failedEvents.contains(event.getType())) {
            log.info("Received Stripe event of type {} which is in failed events list. Processing as failure.", event.getType());
            //process the failed event
            triggerFailedPayment(event);
    }
}
    private void triggerSuccessPayment(CheckoutSessionResponse response) {
        // Implement the logic to handle successful payment events
        log.info("Triggering success payment logic for stripe session id: {}, payment status: {}",
            response.getId(), response.getPaymentStatus());
        // Add your success handling logic here
    }
    private void triggerFailedPayment(Event event) {
        // Implement the logic to handle failed payment events
        log.info("Triggering failed payment logic for stripe event id: {}, type: {}",
            event.getId(), event.getType());
        // Add your failure handling logic here
    }
    //we received header and non-empty body
        public Event isSignatureValid(String stripeSignature, String jsonRequest) {
            try {
            Event event = Webhook.constructEvent(
                jsonRequest, stripeSignature, endpointSecret
            );
            log.info("Stripe event constructed successfully: {}", event.getId());
            return event;
        } catch (SignatureVerificationException e) {
            // Invalid signature
            log.error("Stripe signature verification failed: {}", e.getMessage());
            throw new StripeProviderException(
                ErrorCodeEnum.STRIPE_SIGNATURE_VERIFICATION_ERROR.getErrorCode(),
                ErrorCodeEnum.STRIPE_SIGNATURE_VERIFICATION_ERROR.getErrorMessage(),
                HttpStatus.BAD_REQUEST
            );
      }
    }
}
