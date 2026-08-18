package com.hulkhiretech.payments.service.interfaces;

import com.hulkhiretech.payments.pojo.PaymentRequest;

public interface PaymentService {
    String validateAndCreatePayment(PaymentRequest paymentRequest);
}
