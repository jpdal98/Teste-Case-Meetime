package com.hubspot.integration.controller;

import com.hubspot.integration.model.dto.CreateContactDto;
import com.hubspot.integration.service.HubSpotService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@RestController
@RequestMapping("/api/hubspot")
public class HubSpotController {

    @Autowired
    private WebClient webClient;

    @Autowired
    private HubSpotService hubSpotService;

    @Value("${HUBSPOT_CLIENT_ID:#{systemEnvironment['HUBSPOT_CLIENT_ID']}}")
    private String clientId;

    @Value("${HUBSPOT_CLIENT_SECRET:#{systemEnvironment['HUBSPOT_CLIENT_SECRET']}}")
    private String clientSecret;

    @Value("${hubspot.redirect.uri}")
    private String redirectUri;

    private static final String HUBSPOT_AUTH_URL = "https://app.hubspot.com/oauth/authorize";

    @GetMapping("/authorize")
    public ResponseEntity<Void> redirectToHubSpot() {
        String url = HUBSPOT_AUTH_URL +
                "?client_id=" + clientId +
                "&redirect_uri=" + redirectUri +
                "&scope=crm.objects.contacts.read%20crm.objects.contacts.write%20oauth";

        return ResponseEntity.status(HttpStatus.FOUND)
                .header("Location", url)
                .build();
    }

    @GetMapping("/callback")
    public ResponseEntity<?> handleOAuthCallback(@RequestParam("code") String code) {
        String tokenUrl = "https://api.hubapi.com/oauth/v1/token";

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("grant_type", "authorization_code");
        requestBody.add("client_id", clientId);
        requestBody.add("client_secret", clientSecret);
        requestBody.add("redirect_uri", redirectUri);
        requestBody.add("code", code);

        return webClient.post()
                .uri(tokenUrl)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(requestBody))
                .retrieve()
                .bodyToMono(String.class)
                .map(ResponseEntity::ok)
                .block();
    }

    @PostMapping("/create-contact")
    public ResponseEntity<String> criarContato(
            @Valid @RequestBody CreateContactDto contactDTO,
            @RequestHeader("Authorization") String authorization) {

        ResponseEntity<String> response = hubSpotService.createContact(contactDTO, authorization);

        if (response.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.ok("Contato criado com sucesso");
        } else {
            return ResponseEntity.status(response.getStatusCode()).body("Erro ao criar contato");
        }
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> receiveWebhook(
            @RequestBody String payload,
            @RequestHeader("X-HubSpot-Signature") String signature,
            HttpServletRequest request) {

        String method = request.getMethod();
        String requestUrl = request.getRequestURL().toString();

        boolean validSignature = hubSpotService.isValidSignature(payload, signature, method, requestUrl);

        if (validSignature) {
            System.out.println("Webhook processado com sucesso: " + payload);
            return ResponseEntity.ok("Webhook processado com sucesso");
        } else {
            System.out.println("Assinatura inválida. Webhook não processado.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Assinatura inválida");
        }
    }
}