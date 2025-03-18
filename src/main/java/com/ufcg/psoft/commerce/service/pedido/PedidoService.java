package com.ufcg.psoft.commerce.service.pedido;

import com.ufcg.psoft.commerce.dto.pedido.PedidoPostPutRequestDTO;
import com.ufcg.psoft.commerce.dto.pedido.PedidoResponseDTO;

import java.util.List;

public interface PedidoService {
    PedidoResponseDTO criar(Long idCliente, String codigoCliente, PedidoPostPutRequestDTO pedidoPostPutRequestDTO);

    PedidoResponseDTO alterar(Long id, String codigo, Long idPedido, PedidoPostPutRequestDTO pedidoPostPutRequestDTO, boolean isFornecedor);

    void remover(Long id, String codigo, Long idPedido, boolean isFornecedor);

    List<PedidoResponseDTO> listar(Long id, String codigo, boolean isFornecedor);

    PedidoResponseDTO recuperar(Long id, Long idUsuario, String codigo, boolean isFornecedor);

    PedidoResponseDTO confirmarPagamento(Long idPedido, Long idCliente, String codigoAcesso);

    PedidoResponseDTO confirmarEntrega(Long idPedido, Long idCliente, String codigoAcesso);

    PedidoResponseDTO pedidoEmRota(Long idPedido, Long idCliente, String codigoAcesso);
}