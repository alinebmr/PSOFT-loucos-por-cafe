package com.ufcg.psoft.commerce.model.pedido;

import com.ufcg.psoft.commerce.model.Cliente;
import com.ufcg.psoft.commerce.model.Entregador;
import com.ufcg.psoft.commerce.model.Fornecedor;

public interface PedidoState {
    public void confirmaPagamento(Cliente cliente);
    public void pedidoPreparado(Fornecedor fornecedor);
    public void comecaEntrega(Entregador entregador);
    public void confirmaEntrega(Cliente cliente);
}
