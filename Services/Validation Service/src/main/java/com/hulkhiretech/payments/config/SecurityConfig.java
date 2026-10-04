package com.hulkhiretech.payments.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.session.DisableEncodeUrlFilter;

import com.hulkhiretech.payments.security.ExceptionHandlerFilter;
import com.hulkhiretech.payments.security.HmacSha256Filter;
import com.hulkhiretech.payments.service.HmacSHA256Service;
import com.hulkhiretech.payments.util.JsonUtil;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor 
public class SecurityConfig {

    private final HmacSHA256Service hmacSHA256Service;
    private final JsonUtil jsonUtil;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())

             .authorizeHttpRequests(auth -> auth
                 .anyRequest().permitAll() )
            
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));
           
           /* .authorizeHttpRequests(auth -> auth
                 .anyRequest().authenticated()   // <-- this is what's active right now
)*/

            /* .authorizeHttpRequests(auth -> auth
            .requestMatchers("/v1/payments/**").permitAll()
            .anyRequest().authenticated())
            .addFilterBefore(new ExceptionHandlerFilter(jsonUtil),DisableEncodeUrlFilter.class)

            .addFilterAfter(new  HmacSha256Filter(hmacSHA256Service, jsonUtil), LogoutFilter.class)
        ;
*/
        return http.build();
    }
}