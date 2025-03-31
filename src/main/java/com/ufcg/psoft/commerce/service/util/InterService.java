package com.ufcg.psoft.commerce.service.util;

import com.ufcg.psoft.commerce.enums.StatusPedidoEnum;
import com.ufcg.psoft.commerce.model.Entregador;
import com.ufcg.psoft.commerce.model.Pedido;
import com.ufcg.psoft.commerce.repository.EntregadorRespository;
import com.ufcg.psoft.commerce.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class InterService {
    @Autowired
    EntregadorRespository entregadorRespository;

    @Autowired
    PedidoRepository pedidoRepository;

    public void atribuirEntregador() {
        List<Pedido> pedidosProntos = pedidoRepository.findByStatusPedidoEnum(StatusPedidoEnum.PRONTO);
    }

    private Entregador getEntregadoresDisponiveis() {
        List<Entregador> entregadores = entregadorRespository.findByDisponivel(true);

        return Collections.min(entregadores, Comparator.comparing(Entregador::getUltimaEntrega));
    }
}