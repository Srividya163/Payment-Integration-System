package com.hulkhiretech.payments.service.impl;

import com.hulkhiretech.payments.constant.ValidatorRuleNameEnum;
import com.hulkhiretech.payments.pojo.PaymentRequest;
import com.hulkhiretech.payments.service.interfaces.BusinessValidator;
import com.hulkhiretech.payments.service.interfaces.PaymentService;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    @Value("${validator.rule-names}")
    private String validatorRuleNames;

    private final ApplicationContext applicationContext;

    @Override
    public String validateAndCreatePayment(PaymentRequest paymentRequest) {
        // TODO: Implement payment validation and creation logic

        if(paymentRequest==null){
            log.warn("PaymentRequest is null");
            return "Invalid Payment Request : null";
        }

        //Split the validationRuleNames into list and iterate each rule and log the rule name
        String[] rules = validatorRuleNames.split(",");
        for(String rule : rules){
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

        String result="From Service payment created successfully!\n" + paymentRequest;
        log.info("Payment Creation result:{}", result);
        return result;
    }

    @PostConstruct
    public void init(){
        log.info("PaymentServiceImpl initialized with validatorRuleNames: {}", validatorRuleNames);
    }
}
