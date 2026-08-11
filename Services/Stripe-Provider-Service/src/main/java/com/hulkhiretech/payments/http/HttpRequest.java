package com.hulkhiretech.payments.http;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

import lombok.Data;

@Data
public class HttpRequest {
    private HttpMethod httpMethod;
    private String url;
    private HttpHeaders httpHeaders;
    private Object requestData;

}

/*
*This class is a DTO used to encapsulate all details required to make an HTTP request, 
such as method, URL, headers, and body. */