package com.ufcg.psoft.commerce.model.pedido;

import com.ufcg.psoft.commerce.model.Cliente;
import com.ufcg.psoft.commerce.model.Entregador;
import com.ufcg.psoft.commerce.model.Fornecedor;

class PedidoStateAdapter implements PedidoState {
    @Override
    public void confirmaPagamento(Cliente cliente) {
    }

    @Override
    public void pedidoPreparado(Fornecedor fornecedor) {
    }

    @Override
    public void comecaEntrega(Entregador entregador) {
    }

    @Override
    public void confirmaEntrega(Cliente cliente) {
    }
}
