package com.example.protocolwriter.ai;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.boot.http.client.ClientHttpRequestFactoryBuilder;
import org.springframework.boot.http.client.ClientHttpRequestFactorySettings;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import java.util.List;
import java.util.Map;

/**
 * Тонкий клиент к <a href="https://github.com/ollama/ollama">Ollama</a> —
 * локальному рантайму open-source LLM (qwen, llama, mistral и др.).
 *
 * Используется блокирующий {@link RestClient}: благодаря Virtual Threads
 * (включены в application.yml) долгие вызовы LLM не занимают платформенные потоки.
 */
@Component
public class OllamaClient {

    private final AiProperties props;
    private final RestClient restClient;

    public OllamaClient(AiProperties props, RestClient.Builder builder) {
        this.props = props;
        var settings = ClientHttpRequestFactorySettings.defaults()
                .withConnectTimeout(props.connectTimeout())
                .withReadTimeout(props.readTimeout());
        this.restClient = builder
                .baseUrl(props.baseUrl())
                .requestFactory(ClientHttpRequestFactoryBuilder.detect().build(settings))
                .build();
    }

    /**
     * Один вызов чата без стриминга.
     *
     * @param systemPrompt роль/правила для модели
     * @param userPrompt   собственно задача с текстом протокола
     * @param format       null — свободный текст; JSON-схема (Map) — structured output Ollama
     * @return содержимое ответа модели
     */
    public String chat(String systemPrompt, String userPrompt, Map<String, Object> format) {
        var request = new ChatRequest(
                props.model(),
                List.of(new ChatMessage("system", systemPrompt), new ChatMessage("user", userPrompt)),
                false,
                Map.of("temperature", props.temperature()),
                format);

        ChatResponse response;
        try {
            response = restClient.post()
                    .uri("/api/chat")
                    .body(request)
                    .retrieve()
                    .body(ChatResponse.class);
        } catch (ResourceAccessException e) {
            throw new AiUnavailableException(
                    "Не удалось подключиться к Ollama (%s). Запустите её: docker compose up -d ollama"
                            .formatted(props.baseUrl()), e);
        } catch (RestClientResponseException e) {
            throw new AiUnavailableException(
                    "Ollama вернула ошибку %d. Убедитесь, что модель скачана: docker compose exec ollama ollama pull %s"
                            .formatted(e.getStatusCode().value(), props.model()), e);
        }

        if (response == null || response.message() == null
                || response.message().content() == null || response.message().content().isBlank()) {
            throw new AiUnavailableException("Модель вернула пустой ответ");
        }
        return response.message().content();
    }

    // Java-фича: records как компактные DTO для JSON-протокола Ollama.

    record ChatMessage(String role, String content) {
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    record ChatRequest(String model, List<ChatMessage> messages, boolean stream,
                       Map<String, Object> options, Map<String, Object> format) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    record ChatResponse(ChatMessage message) {
    }
}
