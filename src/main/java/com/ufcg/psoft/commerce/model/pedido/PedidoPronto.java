package com.ufcg.psoft.commerce.model.pedido;

import com.ufcg.psoft.commerce.enums.StatusPedidoEnum;
import com.ufcg.psoft.commerce.exception.EntregadorNaoAprovadoException;
import com.ufcg.psoft.commerce.model.Entregador;
import com.ufcg.psoft.commerce.model.Pedido;

public class PedidoPronto extends PedidoStateAdapter {
    private Pedido pedido;

    public PedidoPronto(Pedido pedido) {
        this.pedido = pedido;
    }

    @Override
    public void comecaEntrega(Entregador entregador) {
        if (!entregador.isAprovado()) {
            throw new EntregadorNaoAprovadoException();
        }
        pedido.setEntregador(entregador);
        pedido.setStatus(StatusPedidoEnum.EM_ENTREGA);
    }
}
