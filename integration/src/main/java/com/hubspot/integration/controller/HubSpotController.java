package com.hubspot.integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hubspot.integration.config.HubSpotProperties;
import com.hubspot.integration.response.ApiResponse;
import com.hubspot.integration.domain.dto.CreateContactDto;
import com.hubspot.integration.service.ContactService;
import com.hubspot.integration.service.OAuthService;
import com.hubspot.integration.service.SignatureService;
import com.hubspot.integration.config.HubSpotConstants;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/hubspot")
public class HubSpotController {

    private final ContactService contactService;
    private final OAuthService oAuthService;
    private final SignatureService signatureService;
    private final HubSpotProperties hubSpotProperties;
    private final ObjectMapper objectMapper;

    public HubSpotController(ContactService contactService,
                             OAuthService oAuthService,
                             SignatureService signatureService,
                             HubSpotProperties hubSpotProperties,
                             ObjectMapper objectMapper) {
        this.contactService = contactService;
        this.oAuthService = oAuthService;
        this.signatureService = signatureService;
        this.hubSpotProperties = hubSpotProperties;
        this.objectMapper = objectMapper;
    }

    @GetMapping("/authorize")
    public ResponseEntity<Void> redirectToHubSpot() {
        String url = String.format("%s?client_id=%s&redirect_uri=%s&scope=%s",
                HubSpotConstants.HUBSPOT_AUTH_URL,
                hubSpotProperties.getClientId(),
                hubSpotProperties.getRedirectUri(),
                HubSpotConstants.HUBSPOT_SCOPE);

        return ResponseEntity.status(HttpStatus.FOUND).header("Location", url).build();
    }

    @GetMapping("/callback")
    public ResponseEntity<ApiResponse<?>> handleOAuthCallback(@RequestParam("code") String code) {
        try {
            var tokenResponse = oAuthService.exchangeCodeForToken(
                    code,
                    hubSpotProperties.getClientId(),
                    hubSpotProperties.getClientSecret(),
                    hubSpotProperties.getRedirectUri()
            );
            return ResponseEntity.ok(ApiResponse.ok("Token obtido com sucesso", tokenResponse.getBody()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Erro ao obter o token: "
                    + e.getMessage(), HttpStatus.BAD_REQUEST.value()));
        }
    }

    @PostMapping("/create-contact")
    public ResponseEntity<ApiResponse<?>> createContact(
            @Valid @RequestBody CreateContactDto contactDTO,
            @RequestHeader("Authorization") String authorization) {
        try {
            var response = contactService.createContact(contactDTO, authorization);
            if (response.getStatusCode().is2xxSuccessful()) {
                return ResponseEntity.ok(ApiResponse.ok("Contato criado com sucesso", response.getBody()));
            } else {
                return ResponseEntity.status(response.getStatusCode())
                        .body(ApiResponse.error("Erro ao criar contato", response.getStatusCode().value()));
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error("Erro ao criar o contato: " + e.getMessage()));
        }
    }

    @PostMapping("/webhook")
    public ResponseEntity<ApiResponse<?>> receiveWebhook(
            @RequestBody List<Map<String, Object>> payload,
            @RequestHeader(HubSpotConstants.SIGNATURE_HEADER) String signature,
            @RequestHeader(HubSpotConstants.TIMESTAMP_HEADER) String timestamp,
            HttpServletRequest request) {

        if (signature == null || timestamp == null) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Faltando cabeçalhos obrigatórios."));
        }

        try {
            String method = request.getMethod();
            String host = request.getHeader("Host");
            String uri = "https://" + host + request.getRequestURI();
            String payloadJson = objectMapper.writeValueAsString(payload);

            if (signatureService.isValidSignature(payloadJson, signature, method, uri, timestamp)) {
                return ResponseEntity.ok(ApiResponse.ok("Webhook processado com sucesso"));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.error(
                        "Assinatura inválida"));
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error("Erro ao processar webhook: " + e.getMessage()));
        }
    }
}