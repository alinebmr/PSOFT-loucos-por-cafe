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
import java.util.NoSuchElementException;

@Service
public class InterService {
    @Autowired
    EntregadorRespository entregadorRespository;

    @Autowired
    PedidoRepository pedidoRepository;

    public void atribuirEntregador() {
        List<Pedido> pedidosProntos = pedidoRepository.findByStatusPedidoEnum(StatusPedidoEnum.PRONTO);

        for(Pedido p : pedidosProntos) {
            Entregador entregador = getEntregadorDisponivel();

            if(entregador == null) {
                break;
            }

            p.setEntregador(entregador);
            p.nextState();
            pedidoRepository.save(p);
        }
    }

    public boolean atribuirEntregador(Pedido pedido) {
        Entregador entregador = getEntregadorDisponivel();

        if(entregador == null) {
            return false;
        }

        pedido.setEntregador(entregador);
        pedido.nextState();

        pedidoRepository.save(pedido);

        return true;
    }

    private Entregador getEntregadorDisponivel() {
        List<Entregador> entregadores = entregadorRespository.findByDisponivel(true);

        try {
            return Collections.min(entregadores, Comparator.comparing(Entregador::getUltimaEntrega));
        } catch (NoSuchElementException e) {
            return null;
        }
    }
}