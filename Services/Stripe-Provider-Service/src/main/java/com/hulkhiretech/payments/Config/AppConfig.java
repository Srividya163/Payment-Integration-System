package com.hulkhiretech.payments.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class AppConfig {

    @Bean
    RestClient restClient(RestClient.Builder builder){
        log.info("Creating RestClient bean.....builder:{}", builder);
        return builder.build();
    }

}
/*
-This code creates a custom HTTP client (RestClient) bean in Spring Boot so your application 
can call external APIs.
-Spring manages it and lets you reuse the same client anywhere using dependency injection.
-It also gives you a place to customize and control how API calls are made. */
