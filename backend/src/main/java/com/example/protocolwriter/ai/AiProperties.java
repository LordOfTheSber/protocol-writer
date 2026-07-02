package com.example.protocolwriter.ai;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.time.Duration;

/**
 * Настройки интеграции с open-source LLM через Ollama.
 *
 * Java-фича: record как иммутабельный носитель конфигурации
 * (constructor binding работает с records из коробки).
 *
 * @param baseUrl        адрес Ollama API (по умолчанию локальный)
 * @param model          имя открытой модели (qwen2.5, llama3.1, mistral и т.д.)
 * @param temperature    «креативность» генерации; для протоколов держим низкой
 * @param connectTimeout таймаут подключения к Ollama
 * @param readTimeout    таймаут генерации (LLM на CPU может отвечать минуты)
 * @param maxInputChars  ограничение длины входного текста (контекст модели)
 */
@ConfigurationProperties(prefix = "app.ai")
public record AiProperties(
        @DefaultValue("http://localhost:11434") String baseUrl,
        @DefaultValue("qwen2.5:7b") String model,
        @DefaultValue("0.3") double temperature,
        @DefaultValue("3s") Duration connectTimeout,
        @DefaultValue("180s") Duration readTimeout,
        @DefaultValue("24000") int maxInputChars) {
}
