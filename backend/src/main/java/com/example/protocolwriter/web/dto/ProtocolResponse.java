package com.example.protocolwriter.web.dto;

import com.example.protocolwriter.domain.ProtocolStatus;
import com.example.protocolwriter.domain.Section;
import com.example.protocolwriter.persistence.ProtocolEntity;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

/** Полное представление протокола. */
public record ProtocolResponse(
        UUID id,
        String title,
        String author,
        ProtocolStatus status,
        List<Section> sections,
        Instant createdAt,
        Instant updatedAt
) {

    public static ProtocolResponse from(ProtocolEntity entity) {
        return new ProtocolResponse(
                entity.getId(),
                entity.getTitle(),
                entity.getAuthor(),
                entity.getStatus(),
                entity.getContent().sections(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
