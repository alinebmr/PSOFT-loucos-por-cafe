package com.ufcg.psoft.commerce.model.pedido;

import com.ufcg.psoft.commerce.enums.StatusPedidoEnum;
import com.ufcg.psoft.commerce.model.Cliente;
import com.ufcg.psoft.commerce.model.Entregador;
import com.ufcg.psoft.commerce.model.Fornecedor;
import com.ufcg.psoft.commerce.model.Pedido;

public class PedidoPronto implements PedidoState {
    private Pedido pedido;

    public PedidoPronto(Pedido pedido) {
        this.pedido = pedido;
    }

    @Override
    public void confirmaPagamento(Cliente cliente) {}

    @Override
    public void pedidoPreparado(Fornecedor fornecedor) {}

    @Override
    public void comecaEntrega(Entregador entregador) {
        // guardar entregador no pedido
        pedido.setStatus(StatusPedidoEnum.EM_ENTREGA);
    }

    @Override
    public void confirmaEntrega() {}
}
