package com.ufcg.psoft.commerce.model.pedido;

import com.ufcg.psoft.commerce.enums.StatusPedidoEnum;
import com.ufcg.psoft.commerce.model.Pedido;

public class PedidoRecebido implements PedidoState {
    private Pedido pedido;

    public PedidoRecebido(Pedido pedido) {
        this.pedido = pedido;
    }

    @Override
    public void nextState() {
        pedido.setStatus(StatusPedidoEnum.PREPARACAO);
    }
}
