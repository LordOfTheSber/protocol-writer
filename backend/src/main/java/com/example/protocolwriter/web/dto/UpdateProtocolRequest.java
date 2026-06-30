package com.example.protocolwriter.web.dto;

import com.example.protocolwriter.domain.ProtocolStatus;
import com.example.protocolwriter.domain.Section;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

/** Запрос на полное обновление протокола. */
public record UpdateProtocolRequest(
        @NotBlank String title,
        String author,
        @NotNull ProtocolStatus status,
        List<Section> sections
) {
}
