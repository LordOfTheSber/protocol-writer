package com.example.protocolwriter.ai;

/** ИИ-функции недоступны: Ollama не запущена, модель не скачана или ответ не разобран. */
public class AiUnavailableException extends RuntimeException {

    public AiUnavailableException(String message) {
        super(message);
    }

    public AiUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }
}
