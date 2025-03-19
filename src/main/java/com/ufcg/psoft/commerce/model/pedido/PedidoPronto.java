package com.ufcg.psoft.commerce.model.pedido;

import com.ufcg.psoft.commerce.enums.StatusPedidoEnum;
import com.ufcg.psoft.commerce.model.Cliente;
import com.ufcg.psoft.commerce.model.Entregador;
import com.ufcg.psoft.commerce.model.Pedido;

public class PedidoPronto implements PedidoState {
    private Pedido pedido;

    public PedidoPronto(Pedido pedido) {
        this.pedido = pedido;
    }

    @Override
    public void nextState() {
        pedido.setStatus(StatusPedidoEnum.EM_ENTREGA);
        notificaCliente();
    }

    private void notificaCliente(){
        System.out.println("O pedido: " + pedido.toString() + " entrou em processo de entrega\n" +
                "Entregador: " + pedido.getEntregador().getNome() + "\n" +
                "Veículo: [Placa: " + pedido.getEntregador().getPlacaVeiculo() + ", Cor : " + pedido.getEntregador().getCorVeiculo() + ", Tipo: " + pedido.getEntregador().getTipoVeiculo() + "]");

    }


}
