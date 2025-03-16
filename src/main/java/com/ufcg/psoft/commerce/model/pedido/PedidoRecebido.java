package com.ufcg.psoft.commerce.model.pedido;

import com.ufcg.psoft.commerce.enums.StatusPedidoEnum;
import com.ufcg.psoft.commerce.exception.ClienteInvalidoException;
import com.ufcg.psoft.commerce.model.Cliente;
import com.ufcg.psoft.commerce.model.Pedido;

public class PedidoRecebido extends PedidoStateAdapter {
    private Pedido pedido;

    public PedidoRecebido(Pedido pedido) {
        this.pedido = pedido;
    }

    @Override
    public void confirmaPagamento(Cliente cliente) {
        if (!cliente.equals(pedido.getCliente())) {
            throw new ClienteInvalidoException();
        }
        pedido.setStatus(StatusPedidoEnum.PREPARACAO);
    }
}
