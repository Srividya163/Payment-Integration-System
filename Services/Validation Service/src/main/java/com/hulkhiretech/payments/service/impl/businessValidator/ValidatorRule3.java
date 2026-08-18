package com.hulkhiretech.payments.service.impl.businessValidator;

import org.springframework.stereotype.Service;

import com.hulkhiretech.payments.pojo.PaymentRequest;
import com.hulkhiretech.payments.service.interfaces.BusinessValidator;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ValidatorRule3 implements BusinessValidator{
    @Override
    public void validate(PaymentRequest paymentRequest){
        log.info("ValidatorRule3: validating payment Request:{}",paymentRequest);

        // Add your validation logic here
        
        log.info("ValidatorRule3: validation passed for payment Request:{}", paymentRequest);
    }

}
