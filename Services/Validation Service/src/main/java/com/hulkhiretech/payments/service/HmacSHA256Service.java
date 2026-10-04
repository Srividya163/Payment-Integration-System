package com.hulkhiretech.payments.service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.hulkhiretech.payments.constant.Constant;
import com.hulkhiretech.payments.constant.ErrorCodeEnum;
import com.hulkhiretech.payments.exception.PaymentValidationException;
import com.hulkhiretech.payments.util.JsonUtil;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class HmacSHA256Service {

    private final JsonUtil jsonUtil;

    @Value("${hmac.secret-key}")
    private String secreteKey;

    public String computeHmacSha256(String jsonInput){
        log.info("Computing HMAC- SHA256 for input: {}", jsonInput);
        //log.info("HMAC INPUT >>> [{}] <<<", jsonInput);

        try{
            SecretKeySpec keySpec =new SecretKeySpec(
                secreteKey.getBytes(StandardCharsets.UTF_8), Constant.HMAC_SHA256);
            Mac mac = Mac.getInstance(Constant.HMAC_SHA256);
            mac.init(keySpec);

            byte[] signatureBytes = mac.doFinal(jsonInput.getBytes(StandardCharsets.UTF_8));

            String hamcSignature = Base64.getEncoder().encodeToString(signatureBytes);

            log.info("HMAC-SHA256 Signature: {}" , hamcSignature);
            return hamcSignature;

        }catch(NoSuchAlgorithmException | InvalidKeyException e){
            e.printStackTrace();
             throw new PaymentValidationException(
                ErrorCodeEnum.HMAC_COMPUTATION_ERROR.getErrorCode(),
                ErrorCodeEnum.HMAC_COMPUTATION_ERROR.getErrorMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    public String isHmacSignatureValid(String jsonString, String headerHmacSignature) {
        if(headerHmacSignature==null || headerHmacSignature.isEmpty()){
            log.error("Missing Hmac Signature in request header");
            throw new PaymentValidationException(
                ErrorCodeEnum.MISSING_HMAC.getErrorCode(),
                ErrorCodeEnum.MISSING_HMAC.getErrorMessage(),
                HttpStatus.UNAUTHORIZED
            );
        }
        
        String calculatedHmac = computeHmacSha256(jsonString);

        if(!calculatedHmac.equals(headerHmacSignature)){
            log.error("Hmac validation failed . Calculated: {}, Received: {}",
             calculatedHmac, headerHmacSignature);
             throw new PaymentValidationException(
                ErrorCodeEnum.INVALID_HMAC.getErrorCode(),
                ErrorCodeEnum.INVALID_HMAC.getErrorMessage(),
                HttpStatus.UNAUTHORIZED);
        }
        return calculatedHmac;
    }

}
