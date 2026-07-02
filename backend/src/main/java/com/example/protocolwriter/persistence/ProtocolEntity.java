package com.example.protocolwriter.persistence;

import com.example.protocolwriter.domain.ProtocolStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

/**
 * JPA-сущность протокола.
 *
 * <p>Тело протокола — единый markdown-документ ({@link #body}). Диаграммы
 * (mermaid / excalidraw) хранятся прямо в тексте как огороженные блоки кода,
 * поэтому бэкенду не нужна отдельная модель секций — он хранит просто текст.
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

    @Column(nullable = false, columnDefinition = "text")
    private String body;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected ProtocolEntity() {
        // для JPA
    }

    public ProtocolEntity(UUID id, String title, String author, ProtocolStatus status, String body) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.status = status;
        this.body = body;
    }

    @PrePersist
    void onCreate() {
        var now = Instant.now();
        this.createdAt = now;
        this.updatedAt = now;
        if (this.body == null) {
            this.body = "";
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

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
