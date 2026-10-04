package com.hulkhiretech.payments.Service.data;

import java.util.ArrayList;
import java.util.List;

import com.hulkhiretech.payments.pojo.LineItems;
import com.hulkhiretech.payments.pojo.Payment;
import com.hulkhiretech.payments.pojo.PaymentRequest;
import com.hulkhiretech.payments.pojo.User;

public class TestDataBuilder {
    public static PaymentRequest buildPaymentRequest(){

        //user
        User user = new User();
        user.setEndUserID("user123");
        user.setFirstname("John");
        user.setLastname("Doe");
        user.setEmail("john.doe@example.com");
        user.setMobilePhone("+1234567890");

        //lineItems
        LineItems item1 = new LineItems();
        item1.setCurrency("EUR");
        item1.setProductName("Phon");
        item1.setUnitAmount(200);
        item1.setQuantity(1);

        LineItems item2 = new LineItems();
        item2.setCurrency("EUR");
        item2.setProductName("Headphones");
        item2.setUnitAmount(500);
        item2.setQuantity(2);

        List<LineItems> lineItems = new ArrayList<>();

        lineItems.add(item1);
        lineItems.add(item2);

        //payment
        Payment payment = new Payment();
        payment.setCurrency("USD");
        payment.setAmount(10000);
        payment.setBrandName("MyShop");
        payment.setLocale("en-US");
        payment.setCountry("US");
        payment.setMerchantTxnRef("TXN1234560017");
        payment.setPaymentMethod("APM");
        payment.setProvider("STRIPE");
        payment.setPaymentType("SALE");
        payment.setSuccessUrl("https://example.com/success");
        payment.setCancelUrl("https://example.com/cancel");
        payment.setLineItems(lineItems);

        //paymentrequest
        PaymentRequest paymentRequest=new PaymentRequest();
        paymentRequest.setUser(user);
        paymentRequest.setPayment(payment);

        return paymentRequest;
    }

}
