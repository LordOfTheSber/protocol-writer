package com.example.protocolwriter.domain;

/**
 * Текстовая секция — заголовок и произвольный текст (например, повестка или
 * вступительное слово).
 *
 * <p>Java-фича: <b>record</b> (Java 16+) — неизменяемый носитель данных с
 * автоматически сгенерированными конструктором, {@code equals}/{@code hashCode}
 * и аксессорами.
 */
public record TextSection(String heading, String body) implements Section {
}
