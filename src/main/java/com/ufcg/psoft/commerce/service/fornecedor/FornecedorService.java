package com.ufcg.psoft.commerce.service.fornecedor;

import com.ufcg.psoft.commerce.dto.entregador.EntregadorResponseDTO;
import com.ufcg.psoft.commerce.dto.fornecedor.*;
import com.ufcg.psoft.commerce.model.Fornecedor;

import java.util.List;

public interface FornecedorService {

    FornecedorResponseDTO alterar(Long id, String codigoAcesso, FornecedorPostPutRequestDTO fornecedorPostPutRequestDTO);

    List<FornecedorResponseDTO> listar();

    FornecedorResponseDTO recuperar(Long id);

    Fornecedor verificaFornecedor(Long id, String codigoAcesso);

    Fornecedor verificaFornecedor(Long idFornecedor);

    FornecedorResponseDTO criar(Long adminId, FornecedorPostPutRequestDTO fornecedorPostPutRequestDTO);

    void remover(Long id, String codigoAcesso);

    List<FornecedorResponseDTO> listarPorNome(String nomeEmpresa);

    EntregadorResponseDTO alterarAprovacaoEntregador(Long fornecedorId, String codigoAcesso, Long entregadorId, boolean aprovado);
}
