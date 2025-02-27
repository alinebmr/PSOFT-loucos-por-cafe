package com.ufcg.psoft.commerce.service.pedido;

import com.ufcg.psoft.commerce.dto.pedido.PedidoResponseDTO;
import com.ufcg.psoft.commerce.model.Endereco;

public interface PedidoService {

    PedidoResponseDTO criar(Long idCliente, Long idCafe, String codigoCliente);

    PedidoResponseDTO criar(Long idCliente, Long idCafe, String codigoCliente, Endereco endereco);
}
