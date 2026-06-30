package com.example.protocolwriter.domain.markdown;

import com.example.protocolwriter.domain.markdown.MarkdownBlock.FencedCode;
import com.example.protocolwriter.domain.markdown.MarkdownBlock.Heading;
import com.example.protocolwriter.domain.markdown.MarkdownBlock.Paragraph;

import java.util.ArrayList;
import java.util.List;

/**
 * Очень простой разборщик markdown в последовательность {@link MarkdownBlock}
 * и расчёт оглавления/статистики.
 *
 * <p>Цель — учебная: показать связку <b>sealed</b> + <b>pattern matching for
 * switch</b> + <b>record patterns</b> (с guard'ами {@code when}) на доменной
 * логике. Это не полноценный CommonMark-парсер: распознаются заголовки
 * ({@code #..}), огороженные блоки кода ({@code ```lang}) и абзацы.
 */
public final class MarkdownAnalyzer {

    private MarkdownAnalyzer() {
    }

    public static List<MarkdownBlock> parse(String body) {
        var blocks = new ArrayList<MarkdownBlock>();
        if (body == null || body.isBlank()) {
            return blocks;
        }

        String[] lines = body.replace("\r\n", "\n").split("\n", -1);
        var paragraph = new StringBuilder();
        boolean inFence = false;
        String fenceLang = "";
        var fenceContent = new StringBuilder();

        for (String line : lines) {
            String trimmed = line.strip();

            if (inFence) {
                if (trimmed.startsWith("```")) {
                    blocks.add(new FencedCode(fenceLang, fenceContent.toString()));
                    inFence = false;
                    fenceLang = "";
                    fenceContent.setLength(0);
                } else {
                    fenceContent.append(line).append('\n');
                }
                continue;
            }

            if (trimmed.startsWith("```")) {
                flushParagraph(paragraph, blocks);
                inFence = true;
                fenceLang = trimmed.substring(3).strip();
                continue;
            }

            if (trimmed.startsWith("#")) {
                flushParagraph(paragraph, blocks);
                int level = 0;
                while (level < trimmed.length() && trimmed.charAt(level) == '#') {
                    level++;
                }
                blocks.add(new Heading(Math.min(level, 6), trimmed.substring(level).strip()));
                continue;
            }

            if (trimmed.isEmpty()) {
                flushParagraph(paragraph, blocks);
            } else {
                if (!paragraph.isEmpty()) {
                    paragraph.append(' ');
                }
                paragraph.append(trimmed);
            }
        }

        if (inFence) {
            blocks.add(new FencedCode(fenceLang, fenceContent.toString()));
        }
        flushParagraph(paragraph, blocks);
        return blocks;
    }

    public static ProtocolOutline outline(String body) {
        var headings = new ArrayList<ProtocolOutline.Heading>();
        for (MarkdownBlock block : parse(body)) {
            // Record pattern: деструктурируем Heading прямо в if.
            if (block instanceof Heading(int level, String text)) {
                headings.add(new ProtocolOutline.Heading(level, text));
            }
        }
        return new ProtocolOutline(headings);
    }

    public static ProtocolStatistics statistics(String body) {
        int words = 0;
        int headings = 0;
        int paragraphs = 0;
        int code = 0;
        int mermaid = 0;
        int excalidraw = 0;

        for (MarkdownBlock block : parse(body)) {
            // Pattern matching for switch + record patterns + guard'ы (when).
            switch (block) {
                case Heading(int ignored, String text) -> {
                    headings++;
                    words += countWords(text);
                }
                case Paragraph(String text) -> {
                    paragraphs++;
                    words += countWords(text);
                }
                case FencedCode(String lang, var ignored) when isMermaid(lang) -> mermaid++;
                case FencedCode(String lang, var ignored) when isExcalidraw(lang) -> excalidraw++;
                case FencedCode ignored -> code++;
            }
        }

        return new ProtocolStatistics(words, headings, paragraphs, code, mermaid, excalidraw);
    }

    private static boolean isMermaid(String lang) {
        return "mermaid".equalsIgnoreCase(lang);
    }

    private static boolean isExcalidraw(String lang) {
        return "excalidraw".equalsIgnoreCase(lang);
    }

    private static int countWords(String text) {
        if (text == null || text.isBlank()) {
            return 0;
        }
        return text.strip().split("\\s+").length;
    }

    private static void flushParagraph(StringBuilder paragraph, List<MarkdownBlock> blocks) {
        if (!paragraph.isEmpty()) {
            blocks.add(new Paragraph(paragraph.toString()));
            paragraph.setLength(0);
        }
    }
}
