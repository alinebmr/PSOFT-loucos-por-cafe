package com.ufcg.psoft.commerce.service.fornecedor;

import com.ufcg.psoft.commerce.dto.fornecedor.*;

import java.util.List;

public interface FornecedorService {

    FornecedorResponseDTO alterar(Long id, String codigoAcesso, FornecedorPostPutRequestDTO clientePostPutRequestDTO);

    List<FornecedorResponseDTO> listar();

    FornecedorResponseDTO recuperar(Long id);

    FornecedorResponseDTO criar(Long adminId, FornecedorPostPutRequestDTO fornecedorPostPutRequestDTO);

    void remover(Long id, String codigoAcesso);

    List<FornecedorResponseDTO> listarPorNome(String nomeEmpresa);
}
