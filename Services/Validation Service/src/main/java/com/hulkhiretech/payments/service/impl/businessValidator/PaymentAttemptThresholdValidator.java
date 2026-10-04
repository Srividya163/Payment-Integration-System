package com.hulkhiretech.payments.service.impl.businessValidator;

import com.hulkhiretech.payments.pojo.PaymentRequest;
import com.hulkhiretech.payments.service.interfaces.BusinessValidator;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.hulkhiretech.payments.repository.interfaces.MerchantPaymentRequestRepository;
import com.hulkhiretech.payments.Cache.ValidatorRuleCache;
import com.hulkhiretech.payments.constant.ErrorCodeEnum;
import com.hulkhiretech.payments.exception.PaymentValidationException;
import com.hulkhiretech.payments.constant.ValidatorRuleNameEnum;


@Service 
@Slf4j 
@RequiredArgsConstructor 
public class PaymentAttemptThresholdValidator implements BusinessValidator {

    private final MerchantPaymentRequestRepository merchantReqRepo;

    private final ValidatorRuleCache validatorRuleCache;
    @Override
    public void validate(PaymentRequest paymentRequest) {
        // Implement validation logic for payment attempt threshold
        log.info("Validating payment request for attempt theshold:{}", paymentRequest);

        Map<String, String> paramsMap = validatorRuleCache.getValidatorParamsForRule(
            ValidatorRuleNameEnum.PAYMENT_ATTEMPT_THRESHOLD_RULE.getRuleName());

        log.info("Validator parameters for rule {}: {}", 
            ValidatorRuleNameEnum.PAYMENT_ATTEMPT_THRESHOLD_RULE.getRuleName(), paramsMap);

        paramsMap.get("durationInMinutes");

        int durationInMinutes = Integer.parseInt(paramsMap.getOrDefault("movingWindowMinutes", "10"));
        int maxPaymentThreshold = Integer.parseInt(paramsMap.getOrDefault("maxAllowedAttempts", "5"));
        int count = merchantReqRepo.countRequestsForUserInLastMinutes(
            paymentRequest.getUser().getEndUserID(), durationInMinutes);

        log.info("Count of payment requests for user {} in last {} minutes: {}", 
            paymentRequest.getUser().getEndUserID(), durationInMinutes, count);

        
        if(count<=maxPaymentThreshold){
            log.info("Payment request is valid , attempt count {} is within threshold {}", 
            count, maxPaymentThreshold);
            return;
        }

        log.error("Payment request exceeds attempt threshold." + "Attempt count: {}, Threshold: {}", 
            count, maxPaymentThreshold);
        throw new PaymentValidationException(
            ErrorCodeEnum.PAYMENT_ATTEMPT_THRESHOLD_EXCEEDED.getErrorCode(),
            ErrorCodeEnum.PAYMENT_ATTEMPT_THRESHOLD_EXCEEDED.getErrorMessage(),
            HttpStatus.TOO_MANY_REQUESTS);
    }

}
