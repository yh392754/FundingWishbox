package com.example.fundingwishbox.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class IamportService {

    private final String apiUrl;
    private final String impKey;
    private final String impSecret;
    private final RestTemplate restTemplate;

    public IamportService(@Value("${iamport.api-url}") String apiUrl,
                          @Value("${iamport.imp-key}") String impKey,
                          @Value("${iamport.imp-secret}") String impSecret,
                          RestTemplate restTemplate) {
        this.apiUrl = apiUrl;
        this.impKey = impKey;
        this.impSecret = impSecret;
        this.restTemplate = restTemplate;
    }

    public String getToken() {
        String url = apiUrl + "/users/getToken";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> body = new HashMap<>();
        body.put("imp_key", impKey);
        body.put("imp_secret", impSecret);

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(body, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(url, entity, Map.class);
        Map responseBody = (Map) response.getBody().get("response");
        return (String) responseBody.get("access_token");
    }

    public Map<String, Object> getPaymentData(String impUid) {
        String token = getToken();
        String url = apiUrl + "/payments/" + impUid;

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);

        return response.getBody();
    }
}
