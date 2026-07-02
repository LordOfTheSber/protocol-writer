package com.example.protocolwriter.ai;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * ИИ-функции поверх open-source LLM: анализ протокола и улучшение текста.
 * Промпты — text blocks, ответы — свободный markdown или structured output (JSON).
 */
@Service
public class AiService {

    private static final String ANALYZE_SYSTEM = """
            Ты — опытный редактор деловых документов. Тебе дают протокол совещания
            в формате Markdown (внутри могут быть блоки диаграмм mermaid/excalidraw —
            не анализируй их содержимое, только факт наличия).
            Проанализируй протокол и верни JSON на русском языке с полями:
            - summary: краткое резюме документа (2–3 предложения);
            - strengths: что в протоколе сделано хорошо;
            - issues: проблемы — неполнота, противоречия, расплывчатые формулировки,
              отсутствие ответственных или сроков;
            - suggestions: конкретные рекомендации, как улучшить текст и структуру;
            - actionItems: список решений и поручений, найденных в тексте
              (с ответственными и сроками, если указаны).
            Пиши кратко и по делу. Если раздел пуст — верни пустой массив.
            """;

    private static final String IMPROVE_SYSTEM = """
            Ты — редактор деловых документов. Тебе дают фрагмент протокола совещания
            в формате Markdown. Выполни задачу и верни ТОЛЬКО итоговый Markdown-текст,
            без пояснений, комментариев и обрамления в блок кода.
            Сохраняй markdown-разметку (заголовки, списки, таблицы) и не трогай
            содержимое блоков кода (```mermaid, ```excalidraw и других).
            Отвечай на языке исходного текста.
            """;

    /** JSON-схема для structured output Ollama — модель обязана вернуть эти поля. */
    private static final Map<String, Object> ANALYSIS_SCHEMA = Map.of(
            "type", "object",
            "required", List.of("summary", "strengths", "issues", "suggestions", "actionItems"),
            "properties", Map.of(
                    "summary", Map.of("type", "string"),
                    "strengths", stringArray(),
                    "issues", stringArray(),
                    "suggestions", stringArray(),
                    "actionItems", stringArray()));

    private final OllamaClient ollama;
    private final AiProperties props;
    private final ObjectMapper objectMapper;

    public AiService(OllamaClient ollama, AiProperties props, ObjectMapper objectMapper) {
        this.ollama = ollama;
        this.props = props;
        this.objectMapper = objectMapper;
    }

    /** Анализ протокола: структура, полнота, решения и поручения. */
    public ProtocolAnalysis analyze(String markdownBody) {
        var prompt = "Протокол:\n\n" + truncate(markdownBody);
        var content = ollama.chat(ANALYZE_SYSTEM, prompt, ANALYSIS_SCHEMA);
        try {
            return objectMapper.readValue(content, ProtocolAnalysis.class);
        } catch (JsonProcessingException e) {
            throw new AiUnavailableException("Модель вернула некорректный JSON — попробуйте ещё раз", e);
        }
    }

    /** Улучшение фрагмента текста в выбранном режиме. */
    public String improve(String text, ImproveMode mode) {
        var prompt = mode.instruction() + "\n\nТекст:\n\n" + truncate(text);
        return stripCodeFence(ollama.chat(IMPROVE_SYSTEM, prompt, null)).strip();
    }

    /** Обрезает вход под контекстное окно модели. */
    private String truncate(String text) {
        var safe = text == null ? "" : text;
        return safe.length() <= props.maxInputChars() ? safe : safe.substring(0, props.maxInputChars());
    }

    /** Модели любят оборачивать ответ в ```markdown … ``` вопреки инструкции — снимаем обёртку. */
    static String stripCodeFence(String content) {
        var trimmed = content.strip();
        if (!trimmed.startsWith("```") || !trimmed.endsWith("```")) {
            return trimmed;
        }
        var withoutClosing = trimmed.substring(0, trimmed.length() - 3);
        var firstLineBreak = withoutClosing.indexOf('\n');
        return firstLineBreak < 0 ? "" : withoutClosing.substring(firstLineBreak + 1);
    }

    private static Map<String, Object> stringArray() {
        return Map.of("type", "array", "items", Map.of("type", "string"));
    }
}
