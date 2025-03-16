package com.ufcg.psoft.commerce.model.pedido;

import com.ufcg.psoft.commerce.enums.StatusPedidoEnum;
import com.ufcg.psoft.commerce.exception.FornecedorInvalidoException;
import com.ufcg.psoft.commerce.model.Fornecedor;
import com.ufcg.psoft.commerce.model.Pedido;

public class PedidoPreparacao extends PedidoStateAdapter {
    private Pedido pedido;

    public PedidoPreparacao(Pedido pedido) {
        this.pedido = pedido;
    }

    @Override
    public void pedidoPreparado(Fornecedor fornecedor) {
        if (!fornecedor.equals(pedido.getCafe().getFornecedor())) {
            throw new FornecedorInvalidoException();
        }
        pedido.setStatus(StatusPedidoEnum.PRONTO);
    }
}
