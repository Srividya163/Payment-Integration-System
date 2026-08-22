package com.hulkhiretech.payments.entity;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class MerchantPaymentRequestEntity {

    private Integer id;
    private String endUserId;
    private String merchantTxnReference;
    private String transactionRequest;
    private LocalDateTime creationDate;
}
