package com.ufcg.psoft.commerce.service.fornecedor;

import com.ufcg.psoft.commerce.exception.ClienteNaoExisteException;
import com.ufcg.psoft.commerce.exception.CodigoDeAcessoInvalidoException;
import com.ufcg.psoft.commerce.repository.FornecedorRepository;
import com.ufcg.psoft.commerce.dto.fornecedor.*;
import com.ufcg.psoft.commerce.model.Fornecedor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FornecedorServiceImpl implements FornecedorService {

    @Autowired
    FornecedorRepository fornecedorRepository;
    @Autowired
    ModelMapper modelMapper;

    @Override
    public FornecedorResponseDTO alterar(Long id, String codigoAcesso, FornecedorPostPutRequestDTO fornecedorPostPutRequestDTO) {
        Fornecedor fornecedor = fornecedorRepository.findById(id).orElseThrow(ClienteNaoExisteException::new);
        if (!fornecedor.getCodigo().equals(codigoAcesso)) {
            throw new CodigoDeAcessoInvalidoException();
        }
        modelMapper.map(fornecedorPostPutRequestDTO, fornecedor);
        fornecedorRepository.save(fornecedor);
        return modelMapper.map(fornecedor, FornecedorResponseDTO.class);
    }

    @Override
    public FornecedorResponseDTO criar(FornecedorPostPutRequestDTO fornecedorPostPutRequestDTO) {
        Fornecedor fornecedor = modelMapper.map(fornecedorPostPutRequestDTO, Fornecedor.class);
        fornecedorRepository.save(fornecedor);
        return modelMapper.map(fornecedor, FornecedorResponseDTO.class);
    }

    @Override
    public void remover(Long id, String codigoAcesso) {
        Fornecedor fornecedor = fornecedorRepository.findById(id).orElseThrow(ClienteNaoExisteException::new);
        if (!fornecedor.getCodigo().equals(codigoAcesso)) {
            throw new CodigoDeAcessoInvalidoException();
        }
        fornecedorRepository.delete(fornecedor);
    }

    @Override
    public List<FornecedorResponseDTO> listarPorNome(String nomeEmpresa) {
        List<Fornecedor> fornecedores = fornecedorRepository.findByNomeContaining(nomeEmpresa);
        return fornecedores.stream()
                .map(FornecedorResponseDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<FornecedorResponseDTO> listar() {
        List<Fornecedor> fornecedores = fornecedorRepository.findAll();
        return fornecedores.stream()
                .map(FornecedorResponseDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public FornecedorResponseDTO recuperar(Long id) {
        Fornecedor fornecedor = fornecedorRepository.findById(id).orElseThrow(ClienteNaoExisteException::new);
        return new FornecedorResponseDTO(fornecedor);
    }
}
