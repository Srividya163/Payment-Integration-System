package com.hulkhiretech.payments.Service;

import lombok.extern.slf4j.Slf4j;
import java.util.Base64;
import org.junit.jupiter.api.Test;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hulkhiretech.payments.Service.data.TestDataBuilder;
import com.hulkhiretech.payments.pojo.PaymentRequest;
import com.hulkhiretech.payments.service.HmacSHA256Service;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import java.lang.reflect.Field;
//import com.hulkhiretech.payments.service.HmacSHA256Service;


@Slf4j
public class HmacSHA256ServiceTest {

    @Test
    public void testComputeHmacSha256ProducesDeterministic32ByteHmac() {
        HmacSHA256Service service =new HmacSHA256Service(null);

        try{
            Field secretKeyField = HmacSHA256Service.class.getDeclaredField("secreteKey");
            secretKeyField.setAccessible(true);
            secretKeyField.set(service, "THIS_IS_MY_SECRET_KEY");
            log.info("SecreteKey set");
        }catch(Exception e){
            e.printStackTrace();
        }

        PaymentRequest requestObj= TestDataBuilder.buildPaymentRequest();

        ObjectMapper objectMapper=new ObjectMapper();
        String dummyJson = null;
        try{
            dummyJson = objectMapper.writeValueAsString(requestObj);
        }catch(Exception e){
            e.printStackTrace();
        }

        String signature = service.computeHmacSha256(dummyJson);

        assertNotNull(signature, "Hmac signature should not be null");

        byte[] decoded = Base64.getDecoder().decode(signature);

        assertEquals(32, decoded.length, "Hmac signature should be 32 bytes long");

        String signature2 = service.computeHmacSha256(dummyJson);
        assertEquals(signature, signature2, "Hmac signature should be deterministic for the same input");
    }
}