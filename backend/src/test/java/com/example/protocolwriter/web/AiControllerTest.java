package com.example.protocolwriter.web;

import com.example.protocolwriter.ai.AiService;
import com.example.protocolwriter.ai.AiUnavailableException;
import com.example.protocolwriter.ai.ImproveMode;
import com.example.protocolwriter.ai.ProtocolAnalysis;
import com.example.protocolwriter.domain.ProtocolStatus;
import com.example.protocolwriter.persistence.ProtocolEntity;
import com.example.protocolwriter.service.ProtocolService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AiController.class)
class AiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProtocolService protocolService;

    @MockitoBean
    private AiService aiService;

    @Test
    void analyzeReturnsStructuredResult() throws Exception {
        var id = UUID.randomUUID();
        var entity = new ProtocolEntity(id, "Протокол №1", "Иванов", ProtocolStatus.DRAFT, "# Текст");
        when(protocolService.findById(id)).thenReturn(entity);
        when(aiService.analyze("# Текст")).thenReturn(new ProtocolAnalysis(
                "Резюме", List.of("Плюс"), List.of("Минус"), List.of("Совет"), List.of("Поручение")));

        mockMvc.perform(post("/api/protocols/{id}/analyze", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.summary").value("Резюме"))
                .andExpect(jsonPath("$.issues[0]").value("Минус"))
                .andExpect(jsonPath("$.actionItems[0]").value("Поручение"));
    }

    @Test
    void improveReturnsText() throws Exception {
        when(aiService.improve(anyString(), eq(ImproveMode.FIX_GRAMMAR))).thenReturn("Исправлено");

        mockMvc.perform(post("/api/ai/improve")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                { "text": "Тикст с ашипками", "mode": "FIX_GRAMMAR" }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text").value("Исправлено"));
    }

    @Test
    void improveWithBlankTextReturns400() throws Exception {
        mockMvc.perform(post("/api/ai/improve")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                { "text": " ", "mode": "IMPROVE" }
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void aiUnavailableReturns503() throws Exception {
        when(aiService.improve(anyString(), any()))
                .thenThrow(new AiUnavailableException("Не удалось подключиться к Ollama"));

        mockMvc.perform(post("/api/ai/improve")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                { "text": "Текст", "mode": "IMPROVE" }
                                """))
                .andExpect(status().isServiceUnavailable())
                .andExpect(jsonPath("$.detail").value("Не удалось подключиться к Ollama"));
    }
}
