package com.ufcg.psoft.commerce.service.entregador;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ufcg.psoft.commerce.dto.entregador.EntregadorPostPutRequestDTO;
import com.ufcg.psoft.commerce.dto.entregador.EntregadorResponseDTO;
import com.ufcg.psoft.commerce.exception.CodigoDeAcessoInvalidoException;
import com.ufcg.psoft.commerce.exception.EntregadorNaoAprovadoException;
import com.ufcg.psoft.commerce.exception.EntregadorNaoExisteException;
import com.ufcg.psoft.commerce.model.Entregador;
import com.ufcg.psoft.commerce.repository.EntregadorRespository;

@Service
public class EntregadorServiceImpl implements EntregadorService {

    @Autowired
    EntregadorRespository entregadorRespository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public List<EntregadorResponseDTO> listar() {
        List<Entregador> entregadores = entregadorRespository.findAll();
        return entregadores.stream().map(EntregadorResponseDTO::new).collect(Collectors.toList());

    }

    @Override
    public EntregadorResponseDTO alterar(Long id, String codigoAcesso, EntregadorPostPutRequestDTO entregadorPostPutRequestDTO) {
        Entregador entregador = entregadorRespository.findById(id).orElseThrow(EntregadorNaoExisteException::new);
        if (!entregador.getCodigo().equals(codigoAcesso)) {
            throw new CodigoDeAcessoInvalidoException();
        }
        modelMapper.map(entregadorPostPutRequestDTO, entregador);
        entregadorRespository.save(entregador);
        return modelMapper.map(entregador, EntregadorResponseDTO.class);
    }


    @Override
    public EntregadorResponseDTO criar(EntregadorPostPutRequestDTO entregadorPostPutRequestDTO) {
        Entregador entregador = modelMapper.map(entregadorPostPutRequestDTO, Entregador.class);
        entregadorRespository.save(entregador);
        return modelMapper.map(entregador, EntregadorResponseDTO.class);
    }

    @Override
    public EntregadorResponseDTO recuperar(Long id) {
        Entregador entregador = entregadorRespository.findById(id).orElseThrow(EntregadorNaoExisteException::new);
        return new EntregadorResponseDTO(entregador);
    }

    @Override
    public void remover(Long id, String codigoAcesso) {
        Entregador entregador = entregadorRespository.findById(id).orElseThrow(EntregadorNaoExisteException::new);
        if (!entregador.getCodigo().equals(codigoAcesso)) {
            throw new CodigoDeAcessoInvalidoException();
        }
        entregadorRespository.delete(entregador);
    }

    @Override
    public List<EntregadorResponseDTO> listarPorNome(String nome) {
        List<Entregador> entregadores = entregadorRespository.findByNomeContaining(nome);

        return entregadores.stream().map(EntregadorResponseDTO::new).collect(Collectors.toList());

    }

    @Override
    public EntregadorResponseDTO alterarAprovacao(Long id, boolean aprovado) {
        Entregador entregador = entregadorRespository.findById(id).orElseThrow(EntregadorNaoExisteException::new);
        entregador.setAprovado(aprovado);
        if (aprovado) {
            entregador.setDisponivel(false);
        }
        entregador = entregadorRespository.save(entregador);
        return new EntregadorResponseDTO(entregador);
    }

    @Override
    public EntregadorResponseDTO modificarDisponibilidade(Long id, String codigoAcesso, boolean disponivel) {
        Entregador entregador = entregadorRespository.findById(id).orElseThrow(EntregadorNaoExisteException::new);
        if (!entregador.getCodigo().equals(codigoAcesso)) {
            throw new CodigoDeAcessoInvalidoException();
        }
        if (!entregador.isAprovado()) {
            throw new EntregadorNaoAprovadoException();
        }
        entregador.setDisponivel(disponivel);
        entregador = entregadorRespository.save(entregador);
        return new EntregadorResponseDTO(entregador);
    }
}
