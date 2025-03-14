package com.ufcg.psoft.commerce.model.pedido;

import com.ufcg.psoft.commerce.enums.StatusPedidoEnum;
import com.ufcg.psoft.commerce.exception.CommerceException;
import com.ufcg.psoft.commerce.model.Cliente;
import com.ufcg.psoft.commerce.model.Entregador;
import com.ufcg.psoft.commerce.model.Fornecedor;
import com.ufcg.psoft.commerce.model.Pedido;

public class PedidoPreparacao implements PedidoState {
    private Pedido pedido;

    public PedidoPreparacao(Pedido pedido) {
        this.pedido = pedido;
    }

    @Override
    public void confirmaPagamento(Cliente cliente) {}

    @Override
    public void pedidoPreparado(Fornecedor fornecedor) {
        if (!fornecedor.equals(pedido.getCafe().getFornecedor())) {
            throw new CommerceException("epa");
        }
        pedido.setStatus(StatusPedidoEnum.PRONTO);
    }

    @Override
    public void comecaEntrega(Entregador entregador) {}

    @Override
    public void confirmaEntrega() {}
}
