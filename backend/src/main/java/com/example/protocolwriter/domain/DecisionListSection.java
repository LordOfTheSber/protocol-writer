package com.example.protocolwriter.domain;

import java.util.List;

/**
 * Секция со списком решений/поручений.
 */
public record DecisionListSection(String heading, List<Decision> decisions) implements Section {

    public DecisionListSection {
        // Компактный канонический конструктор record'а: защищаемся от null-списка.
        decisions = decisions == null ? List.of() : List.copyOf(decisions);
    }
}
