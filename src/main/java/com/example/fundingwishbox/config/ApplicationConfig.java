package com.example.fundingwishbox.config;

import com.example.fundingwishbox.service.IamportService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ApplicationConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public IamportService iamportService(RestTemplate restTemplate,
                                         @Value("${iamport.api-url}") String apiUrl,
                                         @Value("${iamport.imp-key}") String impKey,
                                         @Value("${iamport.imp-secret}") String impSecret) {
        return new IamportService(apiUrl, impKey, impSecret, restTemplate);
    }
}

