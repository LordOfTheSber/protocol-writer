package com.example.protocolwriter.web.dto;

import com.example.protocolwriter.domain.ProtocolStatus;
import com.example.protocolwriter.persistence.ProtocolEntity;

import java.time.Instant;
import java.util.UUID;

/** Полное представление протокола. */
public record ProtocolResponse(
        UUID id,
        String title,
        String author,
        ProtocolStatus status,
        String body,
        Instant createdAt,
        Instant updatedAt
) {

    public static ProtocolResponse from(ProtocolEntity entity) {
        return new ProtocolResponse(
                entity.getId(),
                entity.getTitle(),
                entity.getAuthor(),
                entity.getStatus(),
                entity.getBody(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
