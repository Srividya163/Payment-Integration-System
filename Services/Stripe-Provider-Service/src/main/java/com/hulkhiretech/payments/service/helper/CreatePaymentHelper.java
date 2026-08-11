package com.hulkhiretech.payments.service.helper;

import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;  
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import com.hulkhiretech.payments.http.HttpRequest;
import com.hulkhiretech.payments.pojo.CreatePaymentReq;
import com.hulkhiretech.payments.pojo.LineItem;
import com.hulkhiretech.payments.stripe.StripeError;
import com.hulkhiretech.payments.stripe.StripeErrorResponse;
import com.hulkhiretech.payments.constant.Constant;
import com.hulkhiretech.payments.exception.StripeProviderException;
import com.hulkhiretech.payments.stripe.CheckoutSessionResponse;
import com.hulkhiretech.payments.constant.ErrorCodeEnum;
import com.hulkhiretech.payments.utl.JsonUtil;

import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.Objects;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class CreatePaymentHelper {

        @Value("${stripe.api.key}")
        private String stripeApikey;

        @Value("${stripe.create.session.url}")
        private String stripeCreateSessionUrl;

        private final JsonUtil jsonUtil;

        public HttpRequest prepareStripeCreateSessionRequest(
            CreatePaymentReq createPaymentReq){  
        
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBasicAuth(stripeApikey,"");
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        //form body
        MultiValueMap<String, String> formUrlEncodedData = prepareFormUrLEncodedData(createPaymentReq);
        log.info("Prepared formUrlEncodedData for Stripe create session request: {}", formUrlEncodedData);
    
        HttpRequest httpRequest = new HttpRequest();

        httpRequest.setHttpMethod(HttpMethod.POST);
        httpRequest.setUrl(stripeCreateSessionUrl);
        httpRequest.setHttpHeaders(httpHeaders);
        httpRequest.setRequestData(formUrlEncodedData);

        log.info("Prepared HttpRequest for creating Stripe session: {}", httpRequest);
        return httpRequest;

      }
      
public static MultiValueMap<String, String> prepareFormUrLEncodedData(CreatePaymentReq request) {

    MultiValueMap<String, String> formUrlEncodedData = new LinkedMultiValueMap<>();

    // Static mode
      formUrlEncodedData.add(Constant.CREATE_SESSION_MODE, 
        Constant.CREATE_SESSION_MODE_PAYMENT);

      formUrlEncodedData.add(Constant.CREATE_SESSION_SUCCESS_URL,
         request.getSuccessUrl());

      formUrlEncodedData.add(Constant.CREATE_SESSION_CANCEL_URL,
         request.getCancelUrl());
    

    // Line Items Loop
    if (request.getLineItems() != null && !request.getLineItems().isEmpty()) {
        for (int i = 0; i < request.getLineItems().size(); i++) {

            LineItem item = request.getLineItems().get(i);

            String baseKey = Constant.LINE_ITEMS + Constant.OPEN_BRACKET +i+ Constant.CLOSED_BRACKET;

            formUrlEncodedData.add(baseKey + Constant.OPEN_BRACKET + Constant.QUANTITY + Constant.CLOSED_BRACKET, String.valueOf(item.getQuantity()));
            formUrlEncodedData.add(baseKey + Constant.OPEN_BRACKET + Constant.PRICE_DATA + Constant.CLOSED_BRACKET + Constant.OPEN_BRACKET + Constant.CURRENCY + Constant.CLOSED_BRACKET, item.getCurrency());
            formUrlEncodedData.add(baseKey + Constant.OPEN_BRACKET + Constant.PRICE_DATA + Constant.CLOSED_BRACKET + Constant.OPEN_BRACKET + Constant.UNIT_AMOUNT + Constant.CLOSED_BRACKET ,String.valueOf(item.getUnitAmount()));
            formUrlEncodedData.add(baseKey + Constant.OPEN_BRACKET + Constant.PRICE_DATA + Constant.CLOSED_BRACKET + Constant.OPEN_BRACKET + Constant.PRODUCT_DATA + Constant.CLOSED_BRACKET + Constant.OPEN_BRACKET + Constant.NAME + Constant.CLOSED_BRACKET, item.getProductName());

        }
    }
    return formUrlEncodedData;
}
  
public CheckoutSessionResponse processStriResponse(ResponseEntity<String> httpResponse) {
    if(httpResponse.getStatusCode().is2xxSuccessful()){
        log.info("Stripe create session API call successful, Status code:{} ,Response body: {}",
            httpResponse.getStatusCode(),httpResponse.getBody());

    CheckoutSessionResponse checkoutSession= jsonUtil.convertJsonToObject(
        httpResponse.getBody(), CheckoutSessionResponse.class);
        log.info("Converted checkoutsessionresponse",checkoutSession);

        if(checkoutSession != null && checkoutSession.getId() != null){
            log.info("Stripe create session created successfully sessionId:{} ,Hosted page URL:{}",
            checkoutSession.getId(),checkoutSession.getUrl());
            //SUCCESS
            return checkoutSession;
        }
        log.error("Stripe API call returned successfully but response body is invalid  status code:{},Response body:{}",
            httpResponse.getStatusCode(),httpResponse.getBody());
    }
    //if we reach here mean call is failed/error
    //4xx or 5xx
    if(httpResponse.getStatusCode().is4xxClientError() ||
     httpResponse.getStatusCode().is5xxServerError()){
        log.error("Stripe create session API call failed, Status code:{} ,Response body:{}",
            httpResponse.getStatusCode(),httpResponse.getBody());

        //use jsonUtil to convert the error response body to StripeErrorResponse object and Log the error details
        StripeErrorResponse stripeError= jsonUtil.convertJsonToObject(
            httpResponse.getBody(), StripeErrorResponse.class);

        if(stripeError != null && stripeError.getError() != null){
            log.error("Stripe error details,type:{},code:{},message:{}",
                stripeError.getError().getType(),
                stripeError.getError().getCode(),
                stripeError.getError().getMessage());
        
                String stripeConcatinatedErrorMessage=prepareStripeErrorMessage(stripeError);
                log.error("Stripe concatenated error message:{}",stripeConcatinatedErrorMessage);

                throw new StripeProviderException(
                    ErrorCodeEnum.STRIPE_API_ERROR.getErrorCode(),
                    stripeConcatinatedErrorMessage,
                    HttpStatus.valueOf(httpResponse.getStatusCode().value())
                );
     }
     log.info("Stripe API call failed with non-Json body, status code:{},Response body:{}",
        httpResponse.getStatusCode(), httpResponse.getBody());
    }
    //success object conversion failed
    //no url then also
    //unable parse error response body to stripeerrorresponse object
    throw new StripeProviderException(
        ErrorCodeEnum.INVALID_STRIPE_RESPONSE.getErrorCode(),
        ErrorCodeEnum.INVALID_STRIPE_RESPONSE.getErrorMessage(),
        HttpStatus.BAD_GATEWAY
    );//since stripe provided incorrect response we can consider it as a bad gateway.Its not our fault but stripe's fault.
}
    public String prepareStripeErrorMessage(StripeErrorResponse stripeErrorResponse){
        StripeError error =stripeErrorResponse.getError();

        return Stream.of(
            error.getType(),
            error.getMessage(),
            error.getCode(),
            error.getParam()
        )
        .filter(Objects::nonNull)
        .map(String::trim)
        .filter(s-> !s.isEmpty())
        .collect(Collectors.joining(" - "));
    }

}