package com.ufcg.psoft.commerce.model.pedido;

import com.ufcg.psoft.commerce.model.Cliente;
import com.ufcg.psoft.commerce.model.Entregador;
import com.ufcg.psoft.commerce.model.Fornecedor;
import com.ufcg.psoft.commerce.model.Pedido;

public class PedidoEntregue implements PedidoState {
    private Pedido pedido;

    public PedidoEntregue(Pedido pedido) {
        this.pedido = pedido;
    }

    @Override
    public void confirmaPagamento(Cliente cliente) {}

    @Override
    public void pedidoPreparado(Fornecedor fornecedor) {}

    @Override
    public void comecaEntrega(Entregador entregador) {}

    @Override
    public void confirmaEntrega() {}
}
