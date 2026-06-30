package com.example.protocolwriter.web;

import com.example.protocolwriter.domain.ProtocolStatistics;
import com.example.protocolwriter.domain.render.RenderFormat;
import com.example.protocolwriter.service.ProtocolService;
import com.example.protocolwriter.web.dto.CreateProtocolRequest;
import com.example.protocolwriter.web.dto.ProtocolResponse;
import com.example.protocolwriter.web.dto.ProtocolSummaryResponse;
import com.example.protocolwriter.web.dto.UpdateProtocolRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/protocols")
public class ProtocolController {

    private final ProtocolService service;

    public ProtocolController(ProtocolService service) {
        this.service = service;
    }

    @GetMapping
    public List<ProtocolSummaryResponse> list() {
        return service.findAll().stream().map(ProtocolSummaryResponse::from).toList();
    }

    @GetMapping("/{id}")
    public ProtocolResponse get(@PathVariable UUID id) {
        return ProtocolResponse.from(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<ProtocolResponse> create(@Valid @RequestBody CreateProtocolRequest request) {
        var created = service.create(request);
        return ResponseEntity
                .created(URI.create("/api/protocols/" + created.getId()))
                .body(ProtocolResponse.from(created));
    }

    @PutMapping("/{id}")
    public ProtocolResponse update(@PathVariable UUID id, @Valid @RequestBody UpdateProtocolRequest request) {
        return ProtocolResponse.from(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        service.delete(id);
    }

    @GetMapping(value = "/{id}/render", produces = MediaType.TEXT_PLAIN_VALUE)
    public String render(@PathVariable UUID id,
                         @RequestParam(defaultValue = "MARKDOWN") RenderFormat format) {
        return service.render(id, format);
    }

    @GetMapping("/{id}/stats")
    public ProtocolStatistics stats(@PathVariable UUID id) {
        return service.statistics(id);
    }
}
