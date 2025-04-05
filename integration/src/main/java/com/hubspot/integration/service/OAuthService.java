package com.hubspot.integration.service;

import com.hubspot.integration.config.HubSpotConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class OAuthService {

    private static final Logger log = LoggerFactory.getLogger(OAuthService.class);

    private final WebClient webClient;

    public OAuthService(WebClient webClient) {
        this.webClient = webClient;
    }

    public ResponseEntity<?> exchangeCodeForToken(String code, String clientId, String clientSecret, String redirectUri) {
        try {
            MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
            requestBody.add("grant_type", "authorization_code");
            requestBody.add("client_id", clientId);
            requestBody.add("client_secret", clientSecret);
            requestBody.add("redirect_uri", redirectUri);
            requestBody.add("code", code);

            String responseBody = webClient.post()
                    .uri(HubSpotConstants.HUBSPOT_API_TOKEN_URL)
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(BodyInserters.fromFormData(requestBody))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            return ResponseEntity.ok(responseBody);
        } catch (Exception e) {
            log.error("Erro ao trocar o código de autorização pelo token", e);
            throw new RuntimeException("Falha ao obter token do HubSpot", e);
        }
    }
}

