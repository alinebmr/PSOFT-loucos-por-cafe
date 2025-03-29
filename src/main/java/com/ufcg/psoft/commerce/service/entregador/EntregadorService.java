package com.ufcg.psoft.commerce.service.entregador;

import java.util.List;

import com.ufcg.psoft.commerce.dto.entregador.EntregadorPostPutRequestDTO;
import com.ufcg.psoft.commerce.dto.entregador.EntregadorResponseDTO;

public interface EntregadorService {

    List<EntregadorResponseDTO> listar();

    EntregadorResponseDTO alterar(Long id, String codigoAcesso,
            EntregadorPostPutRequestDTO entregadorPostPutRequestDTO);

    EntregadorResponseDTO criar(EntregadorPostPutRequestDTO entregadorPostPutRequestDTO);

    EntregadorResponseDTO recuperar(Long id);

    void remover(Long id, String codigoAcesso);

    List<EntregadorResponseDTO> listarPorNome(String nome);

    EntregadorResponseDTO alterarAprovacao(Long id, boolean aprovado);

    EntregadorResponseDTO modificarDisponibilidade(Long id, String codigoAcesso, boolean disponivel);
}
