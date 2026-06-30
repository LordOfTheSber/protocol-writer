package com.example.protocolwriter.domain.markdown;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MarkdownAnalyzerTest {

    private static final String BODY = """
            # Протокол совещания

            Вступительный абзац из нескольких слов.

            ## Решения

            Текст решения.

            ```mermaid
            flowchart TD
              A --> B
            ```

            ```excalidraw
            {"id":"d1","elements":[]}
            ```

            ```java
            System.out.println("hi");
            ```
            """;

    @Test
    void parsesBlocksAndCountsStatistics() {
        var stats = MarkdownAnalyzer.statistics(BODY);

        assertThat(stats.headingCount()).isEqualTo(2);
        assertThat(stats.paragraphCount()).isEqualTo(2);
        assertThat(stats.mermaidDiagrams()).isEqualTo(1);
        assertThat(stats.excalidrawDrawings()).isEqualTo(1);
        assertThat(stats.codeBlocks()).isEqualTo(1);
        assertThat(stats.wordCount()).isGreaterThan(0);
    }

    @Test
    void buildsOutlineFromHeadings() {
        var outline = MarkdownAnalyzer.outline(BODY);

        assertThat(outline.headings()).hasSize(2);
        assertThat(outline.headings().getFirst().level()).isEqualTo(1);
        assertThat(outline.headings().getFirst().text()).isEqualTo("Протокол совещания");
        assertThat(outline.headings().getLast().text()).isEqualTo("Решения");
    }

    @Test
    void emptyBodyProducesNoBlocks() {
        assertThat(MarkdownAnalyzer.parse("")).isEmpty();
        assertThat(MarkdownAnalyzer.statistics("").wordCount()).isZero();
        assertThat(MarkdownAnalyzer.outline("  ").headings()).isEmpty();
    }
}
