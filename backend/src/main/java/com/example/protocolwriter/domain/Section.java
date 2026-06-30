package com.example.protocolwriter.domain;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Секция протокола.
 *
 * <p>Java-фича: <b>sealed interface</b> (Java 17+). Перечень разрешённых
 * наследников зафиксирован в {@code permits}, поэтому компилятор знает полный
 * набор вариантов. Благодаря этому {@code switch} по {@link Section} может быть
 * <i>исчерпывающим</i> (exhaustive) без ветки {@code default} — см.
 * {@link com.example.protocolwriter.domain.render.ProtocolRenderer}.
 *
 * <p>Аннотации Jackson задают полиморфную (de)сериализацию в JSON по полю
 * {@code "type"}, что позволяет хранить разнородные секции в одной JSONB-колонке.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = TextSection.class, name = "text"),
        @JsonSubTypes.Type(value = DecisionListSection.class, name = "decisions"),
        @JsonSubTypes.Type(value = TableSection.class, name = "table")
})
public sealed interface Section permits TextSection, DecisionListSection, TableSection {

    /** Заголовок секции (может быть пустым). */
    String heading();
}
