package com.example.protocolwriter.web.dto;

import com.example.protocolwriter.domain.ProtocolStatus;
import jakarta.validation.constraints.NotBlank;

/**
 * Запрос на создание протокола. {@code body} — markdown-текст (может быть пустым).
 */
public record CreateProtocolRequest(
        @NotBlank String title,
        String author,
        ProtocolStatus status,
        String body
) {
}
