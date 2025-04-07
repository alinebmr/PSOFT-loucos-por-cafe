package com.ufcg.psoft.commerce.enums;

public enum StatusPedidoEnum {
    RECEBIDO,
    PREPARACAO,
    PRONTO,
    EM_ENTREGA,
    ENTREGUE;

    @Override
    public String toString() {
        return name(); 
    }

}
