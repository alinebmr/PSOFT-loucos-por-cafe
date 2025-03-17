package com.ufcg.psoft.commerce.service.admin;

import java.util.List;

import com.ufcg.psoft.commerce.dto.admin.*;

public interface AdminService {

    AdminResponseDTO alterar(Long id, String codigoAcesso, AdminPostPutRequestDTO AdminPostPutRequestDTO);

    List<AdminResponseDTO> listar();

    AdminResponseDTO recuperar(Long id);

    AdminResponseDTO criar(AdminPostPutRequestDTO AdminPostPutRequestDTO);

    void remover(Long id, String codigoAcesso);

    List<AdminResponseDTO> listarPorNome(String nome);
}