package com.example.protocolwriter.web;

import com.example.protocolwriter.ai.AiService;
import com.example.protocolwriter.ai.ProtocolAnalysis;
import com.example.protocolwriter.service.ProtocolService;
import com.example.protocolwriter.web.dto.ImproveTextRequest;
import com.example.protocolwriter.web.dto.ImproveTextResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/** ИИ-функции: анализ протокола и улучшение текста через open-source LLM (Ollama). */
@RestController
@RequestMapping("/api")
public class AiController {

    private final ProtocolService protocolService;
    private final AiService aiService;

    public AiController(ProtocolService protocolService, AiService aiService) {
        this.protocolService = protocolService;
        this.aiService = aiService;
    }

    @PostMapping("/protocols/{id}/analyze")
    public ProtocolAnalysis analyze(@PathVariable UUID id) {
        return aiService.analyze(protocolService.findById(id).getBody());
    }

    @PostMapping("/ai/improve")
    public ImproveTextResponse improve(@Valid @RequestBody ImproveTextRequest request) {
        return new ImproveTextResponse(aiService.improve(request.text(), request.mode()));
    }
}
