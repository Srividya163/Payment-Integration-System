package com.hulkhiretech.payments.service.impl;

import com.hulkhiretech.payments.Cache.ValidatorRuleCache;
import com.hulkhiretech.payments.constant.ErrorCodeEnum;
import com.hulkhiretech.payments.constant.ValidatorRuleNameEnum;
import com.hulkhiretech.payments.exception.PaymentValidationException;
import com.hulkhiretech.payments.pojo.PaymentRequest;
import com.hulkhiretech.payments.service.interfaces.BusinessValidator;
import com.hulkhiretech.payments.service.interfaces.PaymentService;
import com.hulkhiretech.payments.service.helper.StripeProviderHelper;
import com.hulkhiretech.payments.http.HttpRequest;
import com.hulkhiretech.payments.http.HttpServiceEngine;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.hulkhiretech.payments.util.JsonUtil;
import com.hulkhiretech.payments.stripeprovider.SPPaymentResponse;
import com.hulkhiretech.payments.pojo.PaymentResponse;
import org.springframework.http.ResponseEntity;


import java.util.Optional;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final ApplicationContext applicationContext;

    private final HttpServiceEngine httpServiceEngine;

    private final StripeProviderHelper stripeProviderHelper;

    private final ValidatorRuleCache validatorRuleCache;

    @Override
    public PaymentResponse validateAndCreatePayment(PaymentRequest paymentRequest) {
        // TODO: Implement payment validation and creation logic

        log.info("Validating and creating payment : {} ", paymentRequest);

        List<String> validatorRules=validatorRuleCache.getValidatorRules();
        log.info("Loaded validator rules from cache:{}", validatorRules);

        if(validatorRules==null || validatorRules.isEmpty()){
            log.warn("No validation rules configured; skipping validation");
            throw new PaymentValidationException(
                ErrorCodeEnum.NO_VALIDATION_RULES_CONFIGURED.getErrorCode(),
                ErrorCodeEnum.NO_VALIDATION_RULES_CONFIGURED.getErrorMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR);
            }
        //Split the validationRuleNames into list and iterate each rule and log the rule name
    
        for(String rule : validatorRules){
            log.info("Validation Rule: {}", rule);

            Optional<Class<? extends BusinessValidator>> validatorClass= 
                                ValidatorRuleNameEnum.getValidatorClassByRule(rule.trim());
                                if(!validatorClass.isPresent()){
                                    log.warn("No validator class found for rule: {}", rule);
                                    continue;
                                }

                                //load the validator bean from application context
                                BusinessValidator validator = applicationContext
                                .getBean(validatorClass.get());

                                if(validator==null){
                                    log.warn("Validator bean is null for rule: {}", rule);
                                    continue;
                                }

                                validator.validate(paymentRequest);
        }

        log.info("All validation rules executed successfully for paymentRequest: {}", 
        paymentRequest);

        //code to invoke processing service
        HttpRequest httpRequest = stripeProviderHelper.createHttpRequest(paymentRequest);
        log.info("Prepared HttpRequest  for stripe provider: {}", httpRequest);

        ResponseEntity<String> httpResponse = httpServiceEngine.makeHttpCall(httpRequest);

        SPPaymentResponse finalResponse = stripeProviderHelper.processResponse(httpResponse);
        PaymentResponse paymentResponse = new PaymentResponse();
        paymentResponse.setHostedPageUrl(finalResponse.getHostedPageUrl());

        log.info("Final PaymentCreation to be returned:{}", paymentResponse);
        return paymentResponse;
    }
}

        