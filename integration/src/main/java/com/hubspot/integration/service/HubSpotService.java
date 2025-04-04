package com.hubspot.integration.service;

import com.hubspot.integration.model.dto.CreateContactDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Service
public class HubSpotService {

    @Value("${HUBSPOT_CLIENT_SECRET:#{systemEnvironment['HUBSPOT_CLIENT_SECRET']}}")
    private String CLIENT_SECRET;

    private static final String HUBSPOT_API_URL = "https://api.hubapi.com/crm/v3/objects/contacts";

    public ResponseEntity<String> createContact(CreateContactDto contactDTO, String accessToken) {
        RestTemplate restTemplate = new RestTemplate();

        // Definição do corpo da requisição
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("properties", Map.of(
                "firstname", contactDTO.firstName(),
                "lastname", contactDTO.lastName(),
                "email", contactDTO.email()
        ));

        // Preenchendo cabeçalhos da requisição
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        // Faz a requisição a Url de criação de contato no HubSpot
        return restTemplate.exchange(HUBSPOT_API_URL, HttpMethod.POST, requestEntity, String.class);
    }

    public boolean isValidSignature(String requestBody, String receivedSignature, String method, String requestUrl) {
        try {
            // Decodificar a URL (caso haja caracteres especiais)
            String decodedUrl = java.net.URLDecoder.decode(requestUrl, StandardCharsets.UTF_8.name());

            // Concatenar os dados para gerar a string que será assinada
            String dataToSign = CLIENT_SECRET + method.toUpperCase() + " "
                    + decodedUrl.replace("http://", "https://")
                    + requestBody;

            // Codificar a string resultante em UTF-8
            String encodedString = new String(dataToSign.getBytes(StandardCharsets.UTF_8));

            // Criar o hash SHA-256
            MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = sha256.digest(encodedString.getBytes(StandardCharsets.UTF_8));

            // Codificar o hash calculado em Base64
            String calculatedSignature = Base64.getEncoder().encodeToString(hashBytes);
            receivedSignature = Base64.getEncoder().encodeToString(hashBytes);

            // Comparar as assinaturas
            return receivedSignature.equals(calculatedSignature);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
