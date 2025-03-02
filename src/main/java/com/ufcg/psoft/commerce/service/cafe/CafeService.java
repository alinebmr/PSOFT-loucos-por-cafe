package com.ufcg.psoft.commerce.service.cafe;

import com.ufcg.psoft.commerce.dto.cafe.*;
import com.ufcg.psoft.commerce.enums.TipoGraoCafe;
import com.ufcg.psoft.commerce.model.Cafe;

import java.util.List;

public interface CafeService {

    CafeResponseDTO alterar(Long idFornecedor, String codigoAcesso, Long idCafe, CafePostPutRequestDTO cafePostPutRequestDTO);

    List<CafeResponseDTO> listar();

    List<CafeResponseDTO> listarFiltraQualidade(Long idCliente);

    List<CafeResponseDTO> listarFiltraQualidadeTipo(Long idCliente, TipoGraoCafe tipo);

    List<CafeResponseDTO> listarPorFornecedor(Long idFornecedor);

    CafeResponseDTO recuperar(Long id);

    Cafe recuperaCafe(Long id);

    CafeResponseDTO criar(Long idFornecedor, String codigoAcesso, CafePostPutRequestDTO cafePostPutRequestDTO);

    void remover(Long idCafe, Long idFornecedor, String codigoAcesso);
}
