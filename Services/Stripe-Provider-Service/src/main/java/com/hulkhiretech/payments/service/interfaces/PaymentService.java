package com.hulkhiretech.payments.service.interfaces;

import com.hulkhiretech.payments.pojo.PaymentResponse;
import com.hulkhiretech.payments.pojo.CreatePaymentReq;


public interface PaymentService {

     public PaymentResponse createPayment(CreatePaymentReq createPaymentReq);
     
    }


