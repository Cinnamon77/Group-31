package com.bookkeeping.service.impl;

import com.bookkeeping.entity.deepseek.ChatRequest;
import com.bookkeeping.entity.deepseek.ChatResponse;
import jakarta.annotation.PostConstruct;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @author bookkeeping
 **/
@Service
public class DeepSeekLegacyClient {
    @Value("${deepseek.api.api-key}")
    private String apiKey;

    @Value("${deepseek.api.base-url}")
    private String baseUrl;

    private RestTemplate restTemplate;

    private final RestTemplateBuilder builder;

    @PostConstruct
    public void init() {
        this.restTemplate = builder
                .rootUri(baseUrl)
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .build();
    }

    public DeepSeekLegacyClient(RestTemplateBuilder builder) {
        this.builder = builder;
        this.restTemplate = builder
                .rootUri(baseUrl)
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .build();
    }

    public ChatResponse chatCompletion(ChatRequest request) throws RuntimeException {
        if (StringUtils.isBlank(apiKey)) {
            throw new RuntimeException("deepseek api key is empty");
        }
        String fullUrl = baseUrl + "/chat/completions";
        HttpEntity<ChatRequest> entity = new HttpEntity<>(request);
        ResponseEntity<ChatResponse> response = restTemplate.postForEntity(
                fullUrl,
                entity,
                ChatResponse.class
        );

        return response.getBody();
    }
}
