package com.ufcg.psoft.commerce.service.pedido;

import com.ufcg.psoft.commerce.dto.pedido.PedidoPostPutRequestDTO;
import com.ufcg.psoft.commerce.dto.pedido.PedidoResponseDTO;

import java.util.List;

public interface PedidoService {

    PedidoResponseDTO criar(Long idCliente, String codigoCliente, PedidoPostPutRequestDTO pedidoPostPutRequestDTO);

    PedidoResponseDTO alterar(Long id, String codigo, Long idPedido, PedidoPostPutRequestDTO pedidoPostPutRequestDTO);

    void remover(Long idCliente, String codigoCliente, Long idPedido);

    List<PedidoResponseDTO> listarPedidoCliente(Long idCliente, String codigoCliente);

    List<PedidoResponseDTO> listarPedidoFornecedor(Long idFornecedor, String codigoFornecedor);
}