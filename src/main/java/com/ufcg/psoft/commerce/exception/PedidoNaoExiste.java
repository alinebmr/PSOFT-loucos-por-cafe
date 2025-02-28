package com.ufcg.psoft.commerce.exception;

public class PedidoNaoExiste extends CommerceException {
    public PedidoNaoExiste() {
        super("O pedido consultado nao existe!");
    }
}
