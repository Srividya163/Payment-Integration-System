package com.hulkhiretech.payments.http;

import com.hulkhiretech.payments.constant.ErrorCodeEnum;
import com.hulkhiretech.payments.exception.StripeProviderException;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import jakarta.annotation.PostConstruct;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class HttpServiceEngine {

    private final RestClient restClient;

    public ResponseEntity<String> makeHttpCall(HttpRequest httpRequest){
            log.info("Making HTTP call to external service...");

    try{        
        ResponseEntity<String> httpResponse = restClient
            .method(httpRequest.getHttpMethod())
            .uri(httpRequest.getUrl())
            .headers((HttpHeaders restClientHttpHeaders)->{
                restClientHttpHeaders.addAll(httpRequest.getHttpHeaders());
            })
            .body(httpRequest.getRequestData())
            .retrieve()
            .toEntity(String.class);

        log.info("HTTP call completed. status code: {}, Response body: {}", 
        httpResponse.getStatusCode(), httpResponse.getBody());

        return httpResponse;
    }catch(HttpClientErrorException | HttpServerErrorException ex){
        log.error("HTTP call failed with client error. status code: {}, Response body: {}", 
        ex.getStatusCode(), ex.getResponseBodyAsString());
        
        if(ex.getStatusCode()==HttpStatus.SERVICE_UNAVAILABLE || ex.getStatusCode()==HttpStatus.GATEWAY_TIMEOUT){
            log.error("Service is Unavailable.status code:{},Response body:{}",
            ex.getStatusCode(),ex.getResponseBodyAsString());

            throw new StripeProviderException(
                ErrorCodeEnum.ERROR_CONNECTING_TO_EXTERNAL_SERVICE.getErrorCode(),
                ErrorCodeEnum.ERROR_CONNECTING_TO_EXTERNAL_SERVICE.getErrorMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
        //prepare ResponseEntity with error details from exception and send it to caller
        ResponseEntity<String> errorResponse=ResponseEntity
                        .status(ex.getStatusCode())
                        .body(ex.getResponseBodyAsString());
        return errorResponse;
    }
    catch(Exception ex){
        //when we don't get http response from stripe.Network error,timeout error or DNS failure etc
        log.error("Unexpected error occurred while making HTTP call.", ex);
        throw new StripeProviderException(
            ErrorCodeEnum.ERROR_CONNECTING_TO_EXTERNAL_SERVICE.getErrorCode(),
            ErrorCodeEnum.ERROR_CONNECTING_TO_EXTERNAL_SERVICE.getErrorMessage(),
            HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
@PostConstruct
public void init(){
    log.info("Initialized HttpServiceEngine...restclient:{}",restClient);
}
}
