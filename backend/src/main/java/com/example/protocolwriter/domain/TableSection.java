package com.example.protocolwriter.domain;

import java.util.List;

/**
 * Табличная секция: список заголовков колонок и строки (каждая строка — список
 * ячеек-строк).
 */
public record TableSection(String heading, List<String> columns, List<List<String>> rows) implements Section {

    public TableSection {
        columns = columns == null ? List.of() : List.copyOf(columns);
        rows = rows == null ? List.of() : rows.stream().map(List::copyOf).toList();
    }
}
