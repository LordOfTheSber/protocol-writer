package com.example.protocolwriter.web.dto;

import com.example.protocolwriter.domain.ProtocolStatus;
import com.example.protocolwriter.domain.Section;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

/**
 * Запрос на создание протокола. {@code sections} может быть {@code null} —
 * тогда протокол создаётся пустым.
 */
public record CreateProtocolRequest(
        @NotBlank String title,
        String author,
        ProtocolStatus status,
        List<Section> sections
) {
}
