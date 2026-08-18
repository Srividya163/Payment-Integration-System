package com.hulkhiretech.payments.service.impl.businessValidator;

import org.springframework.stereotype.Service;

import com.hulkhiretech.payments.pojo.PaymentRequest;
import com.hulkhiretech.payments.service.interfaces.BusinessValidator;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ValidatorRule1 implements BusinessValidator{
    @Override
    public void validate(PaymentRequest paymentRequest){
        log.info("ValidatorRule1: validating payment Request:{}", paymentRequest);

        
        String lastName = paymentRequest.getUser().getLastname();
        if(lastName.contains("hello")){
            throw new RuntimeException("Validation failed: lastName contains hello");
        }

        log.info("ValidatorRule1: validation passed for payment Request:{}", paymentRequest);
    }

}
