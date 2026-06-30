package com.example.protocolwriter.domain.markdown;

/**
 * Статистика по markdown-телу протокола.
 */
public record ProtocolStatistics(
        int wordCount,
        int headingCount,
        int paragraphCount,
        int codeBlocks,
        int mermaidDiagrams,
        int excalidrawDrawings
) {
}
