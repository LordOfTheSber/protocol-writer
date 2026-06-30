package com.example.protocolwriter.domain;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ProtocolStatisticsTest {

    @Test
    void countsSectionsAndDecisionsByStatus() {
        var content = new ProtocolContent(List.of(
                new TextSection("Вступление", "..."),
                new DecisionListSection("Решения", List.of(
                        new Decision("a", DecisionStatus.OPEN, null, null),
                        new Decision("b", DecisionStatus.OPEN, null, null),
                        new Decision("c", DecisionStatus.DONE, null, null)
                )),
                new TableSection("Таблица", List.of("k"), List.of(List.of("v")))
        ));

        var stats = ProtocolStatistics.of(content);

        assertThat(stats.sectionCount()).isEqualTo(3);
        assertThat(stats.textSections()).isEqualTo(1);
        assertThat(stats.decisionSections()).isEqualTo(1);
        assertThat(stats.tableSections()).isEqualTo(1);
        assertThat(stats.totalDecisions()).isEqualTo(3);
        assertThat(stats.decisionsByStatus().get(DecisionStatus.OPEN)).isEqualTo(2);
        assertThat(stats.decisionsByStatus().get(DecisionStatus.DONE)).isEqualTo(1);
        assertThat(stats.decisionsByStatus().get(DecisionStatus.REJECTED)).isEqualTo(0);
    }
}
