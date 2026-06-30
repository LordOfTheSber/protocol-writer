package com.example.protocolwriter.domain.render;

import com.example.protocolwriter.domain.Decision;
import com.example.protocolwriter.domain.DecisionListSection;
import com.example.protocolwriter.domain.ProtocolContent;
import com.example.protocolwriter.domain.Section;
import com.example.protocolwriter.domain.TableSection;
import com.example.protocolwriter.domain.TextSection;

import java.util.List;
import java.util.StringJoiner;

/**
 * Рендер протокола в Markdown или плоский текст.
 *
 * <p>Это центральная учебная точка проекта. Здесь демонстрируются:
 * <ul>
 *   <li><b>Pattern matching for switch</b> (Java 21) — {@code switch} по типу
 *       {@link Section}. Поскольку {@code Section} — sealed, switch исчерпывающий
 *       и не требует {@code default}: добавление нового типа секции вызовет
 *       ошибку компиляции, пока не обработаем новый вариант.</li>
 *   <li><b>Record patterns</b> (Java 21) — деструктуризация record'ов прямо в
 *       метках case: {@code case TextSection(String heading, String body)}.</li>
 * </ul>
 */
public final class ProtocolRenderer {

    private ProtocolRenderer() {
    }

    public static String render(String title, ProtocolContent content, RenderFormat format) {
        return switch (format) {
            case MARKDOWN -> renderMarkdown(title, content);
            case TEXT -> renderText(title, content);
        };
    }

    private static String renderMarkdown(String title, ProtocolContent content) {
        var sb = new StringBuilder();
        sb.append("# ").append(title).append("\n\n");
        for (Section section : content.sections()) {
            sb.append(renderSectionMarkdown(section)).append('\n');
        }
        return sb.toString().stripTrailing() + "\n";
    }

    /**
     * Рендер одной секции в Markdown.
     *
     * <p>Record patterns деструктурируют каждую секцию на компоненты прямо в
     * метке {@code case}.
     */
    private static String renderSectionMarkdown(Section section) {
        return switch (section) {
            case TextSection(String heading, String body) ->
                    headingMarkdown(heading) + body + "\n";

            case DecisionListSection(String heading, List<Decision> decisions) -> {
                var sb = new StringBuilder(headingMarkdown(heading));
                for (Decision d : decisions) {
                    sb.append("- ").append(decisionLine(d)).append('\n');
                }
                yield sb.toString();
            }

            case TableSection(String heading, List<String> columns, List<List<String>> rows) -> {
                var sb = new StringBuilder(headingMarkdown(heading));
                sb.append("| ").append(String.join(" | ", columns)).append(" |\n");
                sb.append("|").append(" --- |".repeat(Math.max(columns.size(), 1))).append('\n');
                for (List<String> row : rows) {
                    sb.append("| ").append(String.join(" | ", row)).append(" |\n");
                }
                yield sb.toString();
            }
        };
    }

    private static String renderText(String title, ProtocolContent content) {
        var sb = new StringBuilder();
        sb.append(title).append('\n');
        sb.append("=".repeat(title.length())).append("\n\n");
        for (Section section : content.sections()) {
            sb.append(renderSectionText(section)).append('\n');
        }
        return sb.toString().stripTrailing() + "\n";
    }

    private static String renderSectionText(Section section) {
        return switch (section) {
            case TextSection(String heading, String body) ->
                    headingText(heading) + body + "\n";

            case DecisionListSection(String heading, List<Decision> decisions) -> {
                var sb = new StringBuilder(headingText(heading));
                for (Decision d : decisions) {
                    sb.append("  * ").append(decisionLine(d)).append('\n');
                }
                yield sb.toString();
            }

            case TableSection(String heading, List<String> columns, List<List<String>> rows) -> {
                var sb = new StringBuilder(headingText(heading));
                sb.append(String.join(" | ", columns)).append('\n');
                for (List<String> row : rows) {
                    sb.append(String.join(" | ", row)).append('\n');
                }
                yield sb.toString();
            }
        };
    }

    /**
     * Текстовое представление одного решения.
     *
     * <p>Record pattern с вложенной деструктуризацией: разбираем {@link Decision}
     * на компоненты и собираем человекочитаемую строку, аккуратно пропуская
     * незаполненные поля.
     */
    private static String decisionLine(Decision decision) {
        return switch (decision) {
            case Decision(String text, var status, String assignee, var dueDate) -> {
                var parts = new StringJoiner(", ");
                parts.add(text);
                parts.add("статус: " + status);
                if (assignee != null && !assignee.isBlank()) {
                    parts.add("отв.: " + assignee);
                }
                if (dueDate != null) {
                    parts.add("срок: " + dueDate);
                }
                yield parts.toString();
            }
        };
    }

    private static String headingMarkdown(String heading) {
        return (heading == null || heading.isBlank()) ? "" : "## " + heading + "\n\n";
    }

    private static String headingText(String heading) {
        return (heading == null || heading.isBlank()) ? "" : heading + ":\n";
    }
}
