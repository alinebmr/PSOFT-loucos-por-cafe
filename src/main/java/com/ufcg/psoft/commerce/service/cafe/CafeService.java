package com.ufcg.psoft.commerce.service.cafe;

import com.ufcg.psoft.commerce.dto.cafe.*;

import java.util.List;

public interface CafeService {

    CafeResponseDTO alterar(Long idFornecedor, String codigoAcesso, Long idCafe, CafePostPutRequestDTO cafePostPutRequestDTO);

    List<CafeResponseDTO> listar();

    List<CafeResponseDTO> listarPorFornecedor(Long idFornecedor, String codigoAcesso);

    CafeResponseDTO recuperar(Long id);

    CafeResponseDTO criar(Long idFornecedor, String codigoAcesso, CafePostPutRequestDTO cafePostPutRequestDTO);

    void remover(Long idFornecedor, String codigoAcesso, Long idCafe);
}
