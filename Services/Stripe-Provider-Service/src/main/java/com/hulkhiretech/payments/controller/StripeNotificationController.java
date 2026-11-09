package com.hulkhiretech.payments.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hulkhiretech.payments.service.StripeNotificationService;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.http.ResponseEntity;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j 
@RestController 
@RequestMapping ("/v1/stripe/notifications")
@RequiredArgsConstructor 
public class StripeNotificationController {

    private final StripeNotificationService stripeNotificationService;

    @PostMapping 
    public ResponseEntity<Void> process(@RequestHeader (value = "Stripe-Signature", required =false) String stripeSignature, 
    @RequestBody String jsonRequest){
        log.info("Received Stripe notification with signature: {}",
        jsonRequest != null ? jsonRequest.length() : 0);

        stripeNotificationService.processNotification(stripeSignature, jsonRequest);

        log.info("Stripe-Signature header: {}", stripeSignature);
        return ResponseEntity.ok().build();
    }
}
