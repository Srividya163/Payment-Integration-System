package com.hulkhiretech.payments.service.impl.businessValidator;

import org.springframework.stereotype.Service;
import com.hulkhiretech.payments.repository.interfaces.MerchantPaymentRequestRepository;
import com.hulkhiretech.payments.constant.ErrorCodeEnum;
import com.hulkhiretech.payments.exception.PaymentValidationException;
import com.hulkhiretech.payments.pojo.PaymentRequest;
import com.hulkhiretech.payments.service.interfaces.BusinessValidator;
import org.springframework.http.HttpStatus;
import com.hulkhiretech.payments.entity.MerchantPaymentRequestEntity;
import com.hulkhiretech.payments.util.JsonUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class DuplicateTxnValidator implements BusinessValidator{

    private final MerchantPaymentRequestRepository repository;
    private final JsonUtil jsonUtil;


    @Override
    public void validate(PaymentRequest paymentRequest){
        log.info("DuplicateTxnValidator: validating payment Request:{}", paymentRequest);

        MerchantPaymentRequestEntity entity = new MerchantPaymentRequestEntity();
        entity.setEndUserId(paymentRequest.getUser().getEndUserID());
        entity.setMerchantTxnReference(paymentRequest.getPayment().getMerchantTxnRef());
        entity.setTransactionRequest(jsonUtil.convertObjectToJson(paymentRequest));

       int pkId =repository.saveMerchantPaymentRequest(entity); 
       //int pkId = new Random().nextInt(10);
        log.info("Repository returned primary key id: {}", pkId);

        if(pkId == -1){
            log.error("Failed to save merchant payment request, possible duplicate transaction reference: {}", 
            paymentRequest.getPayment().getMerchantTxnRef());
            throw new PaymentValidationException(
                    ErrorCodeEnum.DUPLICATE_TRANSACTION.getErrorCode(),
                    ErrorCodeEnum.DUPLICATE_TRANSACTION.getErrorMessage(),
                    HttpStatus.BAD_REQUEST
            );
        }
        log.info("Payment request is valid," +
            "no duplicate transaction detected. Payment request: {}", paymentRequest);
    }

}
