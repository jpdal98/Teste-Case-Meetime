package com.hubspot.integration.domain.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Map;

public record CreateContactDto(

        @NotBlank
        @Size(max = 100, message = "O primeiro nome deve ter no m\u00e1ximo 100 caracteres")
        String firstName,

        @NotBlank
        @Size(max = 100, message = "O sobrenome deve ter no m\u00e1ximo 100 caracteres")
        String lastName,

        @NotBlank
        @Email(message = "O e-mail informado n\u00e3o \u00e9 v\u00e1lido")
        @Size(max = 255, message = "O e-mail deve ter no m\u00e1ximo 255 caracteres")
        String email

) {
        public Map<String, String> toHubSpotProperties() {
                return Map.of(
                        "firstname", firstName,
                        "lastname", lastName,
                        "email", email
                );
        }
}
