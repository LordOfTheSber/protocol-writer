package com.example.protocolwriter.domain.render;

import com.example.protocolwriter.domain.Decision;
import com.example.protocolwriter.domain.DecisionListSection;
import com.example.protocolwriter.domain.DecisionStatus;
import com.example.protocolwriter.domain.ProtocolContent;
import com.example.protocolwriter.domain.TableSection;
import com.example.protocolwriter.domain.TextSection;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ProtocolRendererTest {

    private final ProtocolContent content = new ProtocolContent(List.of(
            new TextSection("Вступление", "Совещание открыто."),
            new DecisionListSection("Решения", List.of(
                    new Decision("Подготовить отчёт", DecisionStatus.OPEN, "Иванов", LocalDate.of(2026, 7, 10)),
                    new Decision("Согласовать бюджет", DecisionStatus.DONE, null, null)
            )),
            new TableSection("Участники", List.of("Имя", "Роль"), List.of(
                    List.of("Иванов", "Аналитик"),
                    List.of("Петров", "Менеджер")
            ))
    ));

    @Test
    void rendersMarkdown() {
        var md = ProtocolRenderer.render("Протокол №1", content, RenderFormat.MARKDOWN);

        assertThat(md).startsWith("# Протокол №1");
        assertThat(md).contains("## Вступление");
        assertThat(md).contains("- Подготовить отчёт, статус: OPEN, отв.: Иванов, срок: 2026-07-10");
        assertThat(md).contains("- Согласовать бюджет, статус: DONE");
        assertThat(md).contains("| Имя | Роль |");
        assertThat(md).contains("| Иванов | Аналитик |");
    }

    @Test
    void rendersPlainText() {
        var text = ProtocolRenderer.render("Протокол №1", content, RenderFormat.TEXT);

        assertThat(text).startsWith("Протокол №1\n===========");
        assertThat(text).contains("Решения:");
        assertThat(text).contains("  * Подготовить отчёт, статус: OPEN, отв.: Иванов, срок: 2026-07-10");
        assertThat(text).contains("Иванов | Аналитик");
    }

    @Test
    void skipsEmptyAssigneeAndDueDate() {
        var single = new ProtocolContent(List.of(
                new DecisionListSection("Решения", List.of(
                        new Decision("Без ответственного", DecisionStatus.IN_PROGRESS, "  ", null)
                ))
        ));

        var md = ProtocolRenderer.render("T", single, RenderFormat.MARKDOWN);

        assertThat(md).contains("- Без ответственного, статус: IN_PROGRESS");
        assertThat(md).doesNotContain("отв.:");
        assertThat(md).doesNotContain("срок:");
    }
}
