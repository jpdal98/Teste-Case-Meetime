package com.hubspot.integration.service;

import com.hubspot.integration.config.HubSpotProperties;
import com.hubspot.integration.config.HubSpotConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
public class SignatureService {

    private static final Logger log = LoggerFactory.getLogger(SignatureService.class);
    private final HubSpotProperties hubSpotProperties;

    public SignatureService(HubSpotProperties hubSpotProperties) {
        this.hubSpotProperties = hubSpotProperties;
    }

    public boolean isValidSignature(String requestBody, String receivedSignature, String method, String uri, String timestamp) {
        try {
            long currentTime = System.currentTimeMillis();
            long timestampLong = Long.parseLong(timestamp);

            // Verificando validade do timestamp da requisição
            if ((currentTime - timestampLong) > HubSpotConstants.MAX_ALLOWED_TIMESTAMP) {
                log.warn("Timestamp do webhook é inválido. timestamp={}, currentTime={}", timestamp, currentTime);
                return false;
            }

            // Montando string que será convertida
            String dataToSign = method + uri + requestBody + timestamp;

            String calculatedSignature = calculateHmacSignature(dataToSign);

            // Realiza comparação temporizada entre as assinaturas
            return timingSafeCompare(calculatedSignature, receivedSignature);

        } catch (Exception e) {
            log.error("Erro ao validar a assinatura do webhook", e);
            return false;
        }
    }

    // Metodo responsável por calcular a assinatura HMAC-SHA256.
    private String calculateHmacSignature(String data) {
        try {
            // Obtém o algoritmo HMAC-SHA256.
            Mac sha256 = Mac.getInstance("HmacSHA256");

            // Cria a chave secreta usando a secret do usuario da configuração.
            SecretKeySpec secretKey = new SecretKeySpec(
                    hubSpotProperties.getClientSecret().getBytes(StandardCharsets.UTF_8),
                    "HmacSHA256");

            // Inicializa o algoritmo HMAC com a chave secreta.
            sha256.init(secretKey);

            // Calcula o hash da string.
            byte[] hashBytes = sha256.doFinal(data.getBytes(StandardCharsets.UTF_8));

            // Retorna a assinatura como uma string codificada em Base64.
            return Base64.getEncoder().encodeToString(hashBytes);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao calcular assinatura HMAC", e);
        }
    }

    private boolean timingSafeCompare(String signature1, String signature2) {
        if (signature1.length() != signature2.length()) {
            return false;
        }

        int result = 0;
        for (int i = 0; i < signature1.length(); i++) {
            result |= signature1.charAt(i) ^ signature2.charAt(i);
        }
        return result == 0;
    }
}
