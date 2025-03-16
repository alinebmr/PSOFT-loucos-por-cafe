package com.ufcg.psoft.commerce.model.pedido;

import com.ufcg.psoft.commerce.enums.StatusPedidoEnum;
import com.ufcg.psoft.commerce.exception.ClienteInvalidoException;
import com.ufcg.psoft.commerce.model.Cliente;
import com.ufcg.psoft.commerce.model.Pedido;

public class PedidoEmEntrega extends PedidoStateAdapter {
    private Pedido pedido;

    public PedidoEmEntrega(Pedido pedido) {
        this.pedido = pedido;
    }

    @Override
    public void confirmaEntrega(Cliente cliente) {
        if (!cliente.equals(pedido.getCliente())) {
            throw new ClienteInvalidoException();
        }
        pedido.setStatus(StatusPedidoEnum.ENTREGUE);
    }
}
