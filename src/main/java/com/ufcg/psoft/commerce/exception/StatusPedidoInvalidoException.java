package com.ufcg.psoft.commerce.exception;

public class StatusPedidoInvalidoException extends CommerceException {
    public StatusPedidoInvalidoException() {
        super("Status do pedido invalido para esta operacacao");
    }
}