package com.ufcg.psoft.commerce.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.ufcg.psoft.commerce.exception.CommerceException;

public enum TipoPagamento {
    DEBITO,
    CREDITO,
    PIX;

    @JsonCreator
    public static TipoPagamento fromString(String key) {
        try {
            return TipoPagamento.valueOf(key.toUpperCase());
        } catch (Exception e) {
            throw new CommerceException("Tipo de pagamento invalido!");
        }
    }

    @JsonValue
    public String getKey() {
        return this.toString();
    }

    public double getDesconto() {
        switch (this) {
            case DEBITO:
                return 0.975;
            case PIX:
                return 0.95;
            default:
                return 1.0;
        }
    }
}
