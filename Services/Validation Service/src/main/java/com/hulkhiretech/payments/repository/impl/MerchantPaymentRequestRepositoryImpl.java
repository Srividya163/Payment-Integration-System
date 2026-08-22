package com.hulkhiretech.payments.repository.impl;

import com.hulkhiretech.payments.entity.MerchantPaymentRequestEntity;
import com.hulkhiretech.payments.repository.interfaces.MerchantPaymentRequestRepository;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.dao.DuplicateKeyException;
import com.hulkhiretech.payments.constant.ErrorCodeEnum;
import com.hulkhiretech.payments.exception.PaymentValidationException;
import org.springframework.http.HttpStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
@RequiredArgsConstructor
public class MerchantPaymentRequestRepositoryImpl 
  implements MerchantPaymentRequestRepository {

    
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private static final String INSERT_SQL ="""
                INSERT INTO validations.merchant_payment_request
                (endUserID,merchantTxnReference,transactionRequest)
                VALUES(:endUserId,:merchantTxnReference,:transactionRequest)
                """;

    @Override
    public int saveMerchantPaymentRequest(MerchantPaymentRequestEntity merchantPaymentRequestEntity) {
        // Implementation for saving the merchant payment request entity
        log.info("Saving Merchant Payment Request Entity : {}",
         merchantPaymentRequestEntity);

        MapSqlParameterSource params = new MapSqlParameterSource();

        params.addValue("endUserId", merchantPaymentRequestEntity.getEndUserId());
        params.addValue("merchantTxnReference", merchantPaymentRequestEntity.getMerchantTxnReference());
        params.addValue("transactionRequest", merchantPaymentRequestEntity.getTransactionRequest());

        KeyHolder keyHolder = new GeneratedKeyHolder();

    try {
        namedParameterJdbcTemplate.update(
                INSERT_SQL,
                params,
                keyHolder,
                new String[]{"id"}
        );

        Number generatedId = keyHolder.getKey();

        if(generatedId != null) {
            log.info("Merchant payment request inserted with id : {}", generatedId);
            return generatedId.intValue();
        }

        log.error("Failed to retrieve generated id after inserting merchant payment request");
        throw new  PaymentValidationException(
                ErrorCodeEnum.FAILED_TO_SAVE_PAYMENT_REQUEST.getErrorCode(),
                ErrorCodeEnum.FAILED_TO_SAVE_PAYMENT_REQUEST.getErrorMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR
        );

    } catch (DuplicateKeyException ex) {

        log.error("Duplicate merchantTxnReference detected :{}",
                merchantPaymentRequestEntity.getMerchantTxnReference(), ex);

        return -1;
    }
  }
}
