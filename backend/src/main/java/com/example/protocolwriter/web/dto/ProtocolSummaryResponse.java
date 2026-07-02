package com.example.protocolwriter.web.dto;

import com.example.protocolwriter.domain.ProtocolStatus;
import com.example.protocolwriter.persistence.ProtocolEntity;

import java.time.Instant;
import java.util.UUID;

/** Краткое представление протокола для списка. */
public record ProtocolSummaryResponse(
        UUID id,
        String title,
        String author,
        ProtocolStatus status,
        Instant updatedAt
) {

    public static ProtocolSummaryResponse from(ProtocolEntity entity) {
        return new ProtocolSummaryResponse(
                entity.getId(),
                entity.getTitle(),
                entity.getAuthor(),
                entity.getStatus(),
                entity.getUpdatedAt()
        );
    }
}
