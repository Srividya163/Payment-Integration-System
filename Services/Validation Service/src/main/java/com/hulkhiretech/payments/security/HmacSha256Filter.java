package com.hulkhiretech.payments.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hulkhiretech.payments.constant.Constant;
import com.hulkhiretech.payments.service.HmacSHA256Service;

import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import java.io.IOException;
import java.util.LinkedHashMap;
import com.hulkhiretech.payments.util.JsonUtil;

@Slf4j
@RequiredArgsConstructor 
public class HmacSha256Filter extends OncePerRequestFilter {

    private final HmacSHA256Service hmacSHA256Service;
    private final JsonUtil jsonUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // TODO: implement HMAC-SHA256 validation logic
        // e.g. read signature header, recompute HMAC over the payload, compare
        log.info("HmacSha256Filter: Processing request for URI: {}", request.getRequestURI());

        log.info("HMAC-SHA256Filter: Reading Hmac-Signature header from request..hmacSha256Service: {}", hmacSHA256Service);
       
        String hmacSignature = request.getHeader("Hmac-Signature");

        WrappedRequest wrappedRequest = new WrappedRequest(request);

        String jsonBody = wrappedRequest.getBody();

        log.info("Read from WrappedRequest body: {}", jsonBody);
        
        String formatedJson = null;
        try{
            formatedJson =  jsonUtil.prepareFormattedJson(jsonBody);
        }catch(Exception e){
            log.error("Error while formatting JSON: {}", e.getMessage());
            throw new AccessDeniedException("Invalid JSON format");
        }

        hmacSHA256Service.isHmacSignatureValid(formatedJson, hmacSignature);

        log.info("HMAC-SHA256 validation passed request for URI: {}", request.getRequestURI());

        SecurityContext context= SecurityContextHolder.createEmptyContext();
            
        Authentication authentication = new HmacAuthenticationToken(Constant.MERCHANT_ID, hmacSignature, Constant.ROLE_MERCHANT);
            
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
        filterChain.doFilter(wrappedRequest, response);

        log.info("HMAC-SHA256 filter completed for request: {}", request.getRequestURI());
        return;
    }
}





