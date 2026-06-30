package com.example.protocolwriter.web.dto;

import com.example.protocolwriter.domain.ProtocolStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/** Запрос на полное обновление протокола. */
public record UpdateProtocolRequest(
        @NotBlank String title,
        String author,
        @NotNull ProtocolStatus status,
        String body
) {
}
