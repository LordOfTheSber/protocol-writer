package com.example.protocolwriter.domain.markdown;

/**
 * Блок markdown-документа, полученный простым разбором тела протокола.
 *
 * <p>Java-фича: <b>sealed interface</b> (Java 17+). Все наследники объявлены в
 * этом же файле, поэтому {@code permits} выводится компилятором, а {@code switch}
 * по {@link MarkdownBlock} в {@link MarkdownAnalyzer} исчерпывающий и не требует
 * ветки {@code default}.
 */
public sealed interface MarkdownBlock {

    /** Заголовок: уровень (число решёток) и текст. */
    record Heading(int level, String text) implements MarkdownBlock {
    }

    /** Блок кода в ограждении ```lang ... ``` (в т.ч. mermaid / excalidraw). */
    record FencedCode(String language, String content) implements MarkdownBlock {
    }

    /** Обычный абзац текста. */
    record Paragraph(String text) implements MarkdownBlock {
    }
}
