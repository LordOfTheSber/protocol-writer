package com.example.protocolwriter.ai;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.Duration;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AiServiceTest {

    private final OllamaClient ollama = mock(OllamaClient.class);
    private final AiProperties props = new AiProperties(
            "http://localhost:11434", "qwen2.5:7b", 0.3,
            Duration.ofSeconds(3), Duration.ofSeconds(180), 100);
    private final AiService service = new AiService(ollama, props, new ObjectMapper());

    @Test
    void analyzeParsesStructuredJson() {
        when(ollama.chat(anyString(), anyString(), any())).thenReturn("""
                {
                  "summary": "Протокол о запуске проекта.",
                  "strengths": ["Есть повестка"],
                  "issues": ["Нет сроков у поручений"],
                  "suggestions": ["Добавить раздел «Решения»"],
                  "actionItems": ["Иванов — подготовить план"]
                }
                """);

        var analysis = service.analyze("# Повестка\n\nОбсудили план.");

        assertThat(analysis.summary()).isEqualTo("Протокол о запуске проекта.");
        assertThat(analysis.issues()).containsExactly("Нет сроков у поручений");
        assertThat(analysis.actionItems()).containsExactly("Иванов — подготовить план");
    }

    @Test
    void analyzeWithBrokenJsonThrowsAiUnavailable() {
        when(ollama.chat(anyString(), anyString(), any())).thenReturn("не json");

        assertThatThrownBy(() -> service.analyze("# Текст"))
                .isInstanceOf(AiUnavailableException.class);
    }

    @Test
    void improvePassesModeInstructionAndTruncatesInput() {
        when(ollama.chat(anyString(), anyString(), isNull())).thenReturn("Готовый текст");

        var longText = "а".repeat(500); // maxInputChars в тесте = 100
        var result = service.improve(longText, ImproveMode.SHORTEN);

        assertThat(result).isEqualTo("Готовый текст");
        var prompt = ArgumentCaptor.forClass(String.class);
        verify(ollama).chat(anyString(), prompt.capture(), isNull());
        assertThat(prompt.getValue()).contains(ImproveMode.SHORTEN.instruction());
        assertThat(prompt.getValue().length()).isLessThan(300);
    }

    @Test
    void improveStripsCodeFenceFromAnswer() {
        when(ollama.chat(anyString(), anyString(), isNull()))
                .thenReturn("```markdown\n# Заголовок\n\nТекст.\n```");

        assertThat(service.improve("текст", ImproveMode.IMPROVE))
                .isEqualTo("# Заголовок\n\nТекст.");
    }

    @Test
    void stripCodeFenceKeepsPlainTextUntouched() {
        assertThat(AiService.stripCodeFence("Обычный текст")).isEqualTo("Обычный текст");
    }

    @Test
    void analyzeRequestsStructuredOutput() {
        when(ollama.chat(anyString(), anyString(), any())).thenReturn("""
                {"summary":"s","strengths":[],"issues":[],"suggestions":[],"actionItems":[]}
                """);

        service.analyze("# Текст");

        ArgumentCaptor<Map<String, Object>> format = ArgumentCaptor.captor();
        verify(ollama).chat(anyString(), anyString(), format.capture());
        assertThat(format.getValue()).containsEntry("type", "object");
    }
}
