package com.example.protocolwriter.domain;

import java.util.EnumMap;
import java.util.Map;

/**
 * Статистика по протоколу: сколько секций каждого типа и сколько решений в
 * каждом статусе.
 *
 * <p>Метод {@link #of(ProtocolContent)} ещё раз показывает pattern matching for
 * switch + record patterns: один проход по секциям с деструктуризацией.
 */
public record ProtocolStatistics(
        int sectionCount,
        int textSections,
        int decisionSections,
        int tableSections,
        int totalDecisions,
        Map<DecisionStatus, Long> decisionsByStatus
) {

    public static ProtocolStatistics of(ProtocolContent content) {
        int text = 0;
        int decisionSections = 0;
        int table = 0;
        int totalDecisions = 0;
        var byStatus = new EnumMap<DecisionStatus, Long>(DecisionStatus.class);
        for (DecisionStatus s : DecisionStatus.values()) {
            byStatus.put(s, 0L);
        }

        for (Section section : content.sections()) {
            switch (section) {
                case TextSection ignored -> text++;
                case TableSection ignored -> table++;
                case DecisionListSection(var heading, var decisions) -> {
                    decisionSections++;
                    totalDecisions += decisions.size();
                    for (Decision d : decisions) {
                        byStatus.merge(d.status(), 1L, Long::sum);
                    }
                }
            }
        }

        return new ProtocolStatistics(
                content.sections().size(), text, decisionSections, table, totalDecisions, byStatus);
    }
}
