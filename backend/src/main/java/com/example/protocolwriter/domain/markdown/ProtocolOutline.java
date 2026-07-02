package com.example.protocolwriter.domain.markdown;

import java.util.List;

/**
 * Оглавление протокола — список заголовков по порядку.
 */
public record ProtocolOutline(List<Heading> headings) {

    public record Heading(int level, String text) {
    }

    public ProtocolOutline {
        headings = headings == null ? List.of() : List.copyOf(headings);
    }
}
