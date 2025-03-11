package com.ufcg.psoft.commerce.service.cafe;

import com.ufcg.psoft.commerce.dto.cafe.*;
import com.ufcg.psoft.commerce.dto.cliente.ClienteResponseDTO;
import com.ufcg.psoft.commerce.model.Cafe;

import java.util.List;

public interface CafeService {

    CafeResponseDTO alterar(Long idFornecedor, String codigoAcesso, Long idCafe, CafePostPutRequestDTO cafePostPutRequestDTO);

    List<CafeResponseDTO> listar();

    List<CafeResponseDTO> listarFiltraQualidade(Long idCliente);

    List<CafeResponseDTO> listarFiltro(Long idCliente, String tipo, String origem, String perfil);

    List<CafeResponseDTO> listarPorFornecedor(Long idFornecedor);

    CafeResponseDTO recuperar(Long id);

    Cafe recuperaCafe(Long id);

    CafeResponseDTO criar(Long idFornecedor, String codigoAcesso, CafePostPutRequestDTO cafePostPutRequestDTO);

    void remover(Long idCafe, Long idFornecedor, String codigoAcesso);

    ClienteResponseDTO demonstrarInteresse(Long idCliente, String codigoAcesso, Long idCafe);

    List<CafeResponseDTO> listarCafesInteresseCliente(Long idCliente, String codigoAcesso);

    void removerInteresseClienteCafe(Long idCliente, String codigoAcesso, Long idCafe);

    CafeResponseDTO alterarDisponibilidadeCafe(Long idCafe, Long idFornecedor, String codigoAcesso, boolean disponibilidade);
}
