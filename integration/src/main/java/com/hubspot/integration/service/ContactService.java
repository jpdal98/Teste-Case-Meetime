package com.hubspot.integration.service;

import com.hubspot.integration.domain.dto.CreateContactDto;
import com.hubspot.integration.config.HubSpotConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class ContactService {

    private static final Logger log = LoggerFactory.getLogger(ContactService.class);

    private final RestTemplate restTemplate;

    public ContactService() {
        this.restTemplate = new RestTemplate();
    }

    public ResponseEntity<String> createContact(CreateContactDto contactDTO, String accessToken) {
        try {
            // Montando o corpo da requisição
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("properties", contactDTO.toHubSpotProperties());

            // Preenchendo cabeçalhos da requisição
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(accessToken);

            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

            // Realizando requisição a Url de criação de contato no HubSpot
            return restTemplate.exchange(HubSpotConstants.HUBSPOT_CONTACT_URL, HttpMethod.POST, requestEntity, String.class);

        } catch (Exception e) {
            log.error("Erro ao criar contato no HubSpot", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao criar contato: " + e.getMessage());
        }
    }
}
