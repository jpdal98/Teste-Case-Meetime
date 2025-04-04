package com.hubspot.integration.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record CreateContactDto(
        @NotBlank
        String firstName,
        @NotBlank
        String lastName,
        @NotBlank
        String email
) { }
