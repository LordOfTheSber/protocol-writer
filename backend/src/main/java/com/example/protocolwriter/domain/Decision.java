package com.example.protocolwriter.domain;

import java.time.LocalDate;

/**
 * Одно решение/поручение: текст, статус, ответственный и срок.
 *
 * <p>{@code assignee} и {@code dueDate} опциональны (могут быть {@code null}).
 */
public record Decision(
        String text,
        DecisionStatus status,
        String assignee,
        LocalDate dueDate
) {
}
