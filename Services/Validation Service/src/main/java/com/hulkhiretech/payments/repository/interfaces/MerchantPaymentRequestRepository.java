package com.hulkhiretech.payments.repository.interfaces;

import com.hulkhiretech.payments.entity.MerchantPaymentRequestEntity;

public interface MerchantPaymentRequestRepository {
    public int saveMerchantPaymentRequest(MerchantPaymentRequestEntity merchantPaymentRequestEntity);

}
