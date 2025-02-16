package com.ufcg.psoft.commerce.service.admin;

import com.ufcg.psoft.commerce.exception.AdminNaoExisteException;
import com.ufcg.psoft.commerce.exception.CodigoDeAcessoInvalidoException;
import com.ufcg.psoft.commerce.repository.AdminRepository;
import com.ufcg.psoft.commerce.dto.admin.*;
import com.ufcg.psoft.commerce.model.Admin;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    AdminRepository AdminRepository;
    @Autowired
    ModelMapper modelMapper;

    @Override
    public AdminResponseDTO alterar(Long id, String codigoAcesso, AdminPostPutRequestDTO AdminPostPutRequestDTO) {
        Admin Admin = AdminRepository.findById(id).orElseThrow(AdminNaoExisteException::new);
        if (!Admin.getCodigo().equals(codigoAcesso)) {
            throw new CodigoDeAcessoInvalidoException();
        }
        modelMapper.map(AdminPostPutRequestDTO, Admin);
        AdminRepository.save(Admin);
        return modelMapper.map(Admin, AdminResponseDTO.class);
    }

    @Override
    public AdminResponseDTO criar(AdminPostPutRequestDTO AdminPostPutRequestDTO) {
        Admin Admin = modelMapper.map(AdminPostPutRequestDTO, Admin.class);
        AdminRepository.save(Admin);
        return modelMapper.map(Admin, AdminResponseDTO.class);
    }

    @Override
    public void remover(Long id, String codigoAcesso) {
        Admin Admin = AdminRepository.findById(id).orElseThrow(AdminNaoExisteException::new);
        if (!Admin.getCodigo().equals(codigoAcesso)) {
            throw new CodigoDeAcessoInvalidoException();
        }
        AdminRepository.delete(Admin);
    }

    @Override
    public List<AdminResponseDTO> listarPorNome(String nome) {
        List<Admin> Admins = AdminRepository.findByNomeContaining(nome);
        return Admins.stream()
                .map(AdminResponseDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<AdminResponseDTO> listar() {
        List<Admin> Admins = AdminRepository.findAll();
        return Admins.stream()
                .map(AdminResponseDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public AdminResponseDTO recuperar(Long id) {
        Admin Admin = AdminRepository.findById(id).orElseThrow(AdminNaoExisteException::new);
        return new AdminResponseDTO(Admin);
    }
}