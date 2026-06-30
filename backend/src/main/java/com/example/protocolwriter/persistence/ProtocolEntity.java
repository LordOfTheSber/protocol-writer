package com.example.protocolwriter.persistence;

import com.example.protocolwriter.domain.ProtocolContent;
import com.example.protocolwriter.domain.ProtocolStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.UUID;

/**
 * JPA-сущность протокола.
 *
 * <p>Поле {@link #content} хранится в PostgreSQL как <b>JSONB</b> благодаря
 * {@code @JdbcTypeCode(SqlTypes.JSON)} — Hibernate (де)сериализует
 * {@link ProtocolContent} через Jackson, а полиморфизм секций обеспечивают
 * аннотации на {@link com.example.protocolwriter.domain.Section}.
 */
@Entity
@Table(name = "protocols")
public class ProtocolEntity {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column
    private String author;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProtocolStatus status;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(nullable = false, columnDefinition = "jsonb")
    private ProtocolContent content;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected ProtocolEntity() {
        // для JPA
    }

    public ProtocolEntity(UUID id, String title, String author, ProtocolStatus status, ProtocolContent content) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.status = status;
        this.content = content;
    }

    @PrePersist
    void onCreate() {
        var now = Instant.now();
        this.createdAt = now;
        this.updatedAt = now;
        if (this.content == null) {
            this.content = ProtocolContent.empty();
        }
        if (this.status == null) {
            this.status = ProtocolStatus.DRAFT;
        }
    }

    @PreUpdate
    void onUpdate() {
        this.updatedAt = Instant.now();
    }

    public UUID getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public ProtocolStatus getStatus() {
        return status;
    }

    public void setStatus(ProtocolStatus status) {
        this.status = status;
    }

    public ProtocolContent getContent() {
        return content;
    }

    public void setContent(ProtocolContent content) {
        this.content = content;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
