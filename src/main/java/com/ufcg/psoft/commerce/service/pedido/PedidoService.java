package com.ufcg.psoft.commerce.service.pedido;

import com.ufcg.psoft.commerce.dto.pedido.PedidoPostPutRequestDTO;
import com.ufcg.psoft.commerce.dto.pedido.PedidoResponseDTO;
import com.ufcg.psoft.commerce.enums.StatusPedidoEnum;

import java.util.List;

public interface PedidoService {
    PedidoResponseDTO criar(Long idCliente, String codigoCliente, PedidoPostPutRequestDTO pedidoPostPutRequestDTO);

    PedidoResponseDTO alterar(Long id, String codigo, Long idPedido, PedidoPostPutRequestDTO pedidoPostPutRequestDTO, boolean isFornecedor);

    List<PedidoResponseDTO> listar(Long id, String codigo, boolean isFornecedor);

    List<PedidoResponseDTO> listarPorStatus(Long id, String codigo, StatusPedidoEnum status);

    PedidoResponseDTO recuperar(Long id, Long idUsuario, String codigo, boolean isFornecedor);

    PedidoResponseDTO confirmarPagamento(Long idPedido, Long idCliente, String codigoAcesso);

    PedidoResponseDTO confirmarEntrega(Long idPedido, Long idCliente, String codigoAcesso);

    PedidoResponseDTO pedidoPronto(Long idPedido, Long idFornecedor, String codigoAcesso);

    void cancelarPedido(Long idPedido, Long idCliente, String codigoAcesso);
}