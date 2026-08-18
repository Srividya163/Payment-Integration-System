package com.hulkhiretech.payments.service.impl.businessValidator;

import org.springframework.stereotype.Service;
import com.hulkhiretech.payments.pojo.PaymentRequest;
import com.hulkhiretech.payments.service.interfaces.BusinessValidator;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class validatorRule2 implements BusinessValidator{
    @Override
    public void validate(PaymentRequest paymentRequest){
        log.info("ValidatorRule2: validating payment Request:{}", paymentRequest);

        String firstName = paymentRequest.getUser().getFirstname();
        if(firstName.contains("hello")){
            throw new RuntimeException("Validation failed: firstName contains hello");
        }

        log.info("ValidatorRule2: validation passed for payment Request:{}", paymentRequest);
    }

}
