package com.example.protocolwriter.service;

import com.example.protocolwriter.domain.ProtocolStatus;
import com.example.protocolwriter.domain.markdown.MarkdownAnalyzer;
import com.example.protocolwriter.domain.markdown.ProtocolOutline;
import com.example.protocolwriter.domain.markdown.ProtocolStatistics;
import com.example.protocolwriter.persistence.ProtocolEntity;
import com.example.protocolwriter.persistence.ProtocolRepository;
import com.example.protocolwriter.web.dto.CreateProtocolRequest;
import com.example.protocolwriter.web.dto.UpdateProtocolRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class ProtocolService {

    private final ProtocolRepository repository;

    public ProtocolService(ProtocolRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<ProtocolEntity> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public ProtocolEntity findById(UUID id) {
        return repository.findById(id).orElseThrow(() -> new ProtocolNotFoundException(id));
    }

    public ProtocolEntity create(CreateProtocolRequest request) {
        var status = request.status() == null ? ProtocolStatus.DRAFT : request.status();
        var body = request.body() == null ? "" : request.body();
        var entity = new ProtocolEntity(UUID.randomUUID(), request.title(), request.author(), status, body);
        return repository.save(entity);
    }

    public ProtocolEntity update(UUID id, UpdateProtocolRequest request) {
        var entity = findById(id);
        entity.setTitle(request.title());
        entity.setAuthor(request.author());
        entity.setStatus(request.status());
        entity.setBody(request.body() == null ? "" : request.body());
        return repository.save(entity);
    }

    public void delete(UUID id) {
        if (!repository.existsById(id)) {
            throw new ProtocolNotFoundException(id);
        }
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public ProtocolStatistics statistics(UUID id) {
        return MarkdownAnalyzer.statistics(findById(id).getBody());
    }

    @Transactional(readOnly = true)
    public ProtocolOutline outline(UUID id) {
        return MarkdownAnalyzer.outline(findById(id).getBody());
    }
}
