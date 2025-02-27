package com.ufcg.psoft.commerce.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.ufcg.psoft.commerce.exception.CommerceException;

public enum TipoGraoCafe {
    GRAO,
    MOIDO,
    CAPSULA;

    @JsonCreator
    public static TipoGraoCafe fromString(String key) {
        try {
            return TipoGraoCafe.valueOf(key.toUpperCase());
        } catch (Exception e) {
            throw new CommerceException("Tipo do cafe invalido!");
        }
    }

    @JsonValue
    public String getKey() {
        return this.toString();
    }
}
