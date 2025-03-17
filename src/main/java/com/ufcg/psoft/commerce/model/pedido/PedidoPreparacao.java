package com.ufcg.psoft.commerce.model.pedido;

import com.ufcg.psoft.commerce.enums.StatusPedidoEnum;
import com.ufcg.psoft.commerce.model.Pedido;

public class PedidoPreparacao implements PedidoState {
    private Pedido pedido;

    public PedidoPreparacao(Pedido pedido) {
        this.pedido = pedido;
    }

    @Override
    public void nextState() {
        pedido.setStatus(StatusPedidoEnum.PRONTO);
    }
}
