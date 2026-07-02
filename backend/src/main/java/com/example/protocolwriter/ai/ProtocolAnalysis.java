package com.example.protocolwriter.ai;

import java.util.List;

/**
 * Структурированный результат AI-анализа протокола.
 * Модель заполняет его через structured output Ollama (JSON-схема).
 *
 * @param summary     краткое резюме документа (2–3 предложения)
 * @param strengths   что в протоколе хорошо
 * @param issues      проблемы: неполнота, противоречия, расплывчатые формулировки
 * @param suggestions рекомендации по улучшению текста и структуры
 * @param actionItems найденные в тексте решения и поручения
 */
public record ProtocolAnalysis(
        String summary,
        List<String> strengths,
        List<String> issues,
        List<String> suggestions,
        List<String> actionItems) {
}
