package com.ufcg.psoft.commerce.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.ufcg.psoft.commerce.exception.CommerceException;

public enum TipoAssinatura {
    PREMIUM,
    NORMAL;

    @JsonCreator
    public static TipoAssinatura fromString(String key) {
        try {
            return TipoAssinatura.valueOf(key.toUpperCase());
        } catch (Exception e) {
            throw new CommerceException("Valor invalido para assinatura");
        }
    }

    @JsonValue
    public String getKey() {
        return this.toString();
    }
}
