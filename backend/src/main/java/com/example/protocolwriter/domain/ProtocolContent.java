package com.example.protocolwriter.domain;

import java.util.List;

/**
 * Содержимое протокола — упорядоченный набор секций.
 *
 * <p>Java-фича: <b>Sequenced Collections</b> (Java 21). {@link List} реализует
 * {@code SequencedCollection}, поэтому доступны {@code getFirst()} /
 * {@code getLast()} — см. {@link #firstSection()} / {@link #lastSection()}.
 * Порядок секций существенен, и Sequenced Collections делают работу с «первой» и
 * «последней» секцией выразительной.
 */
public record ProtocolContent(List<Section> sections) {

    public ProtocolContent {
        sections = sections == null ? List.of() : List.copyOf(sections);
    }

    public static ProtocolContent empty() {
        return new ProtocolContent(List.of());
    }

    public boolean isEmpty() {
        return sections.isEmpty();
    }

    /** Первая секция (Sequenced Collections: {@code getFirst}). */
    public Section firstSection() {
        return sections.getFirst();
    }

    /** Последняя секция (Sequenced Collections: {@code getLast}). */
    public Section lastSection() {
        return sections.getLast();
    }
}
