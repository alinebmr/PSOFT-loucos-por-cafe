package com.ufcg.psoft.commerce.service.cafe;

import com.ufcg.psoft.commerce.exception.CafeNaoExisteException;
import com.ufcg.psoft.commerce.service.fornecedor.FornecedorService;
import com.ufcg.psoft.commerce.service.cliente.ClienteService;
import com.ufcg.psoft.commerce.repository.CafeRepository;
import com.ufcg.psoft.commerce.dto.cafe.CafePostPutRequestDTO;
import com.ufcg.psoft.commerce.dto.cafe.CafeResponseDTO;
import com.ufcg.psoft.commerce.dto.cliente.ClienteResponseDTO;
import com.ufcg.psoft.commerce.enums.QualidadeCafe;
import com.ufcg.psoft.commerce.enums.TipoAssinatura;
import com.ufcg.psoft.commerce.enums.TipoGraoCafe;
import com.ufcg.psoft.commerce.model.Cafe;
import com.ufcg.psoft.commerce.model.Fornecedor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CafeServiceImpl implements CafeService{

    @Autowired
    FornecedorService fornecedorService;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    CafeRepository cafeRepository;
    @Autowired
    ClienteService clienteService;

    @Override
    public CafeResponseDTO criar(Long idFornecedor, String codigoAcesso, CafePostPutRequestDTO cafePostPutRequestDTO) {
        Fornecedor fornecedor = fornecedorService.verificaFornecedor(idFornecedor, codigoAcesso);

        Cafe cafe = modelMapper.map(cafePostPutRequestDTO, Cafe.class);
        cafe.setFornecedor(fornecedor);
        cafeRepository.save(cafe);
        return modelMapper.map(cafe, CafeResponseDTO.class);
    }

    @Override
    public void remover(Long idCafe, Long idFornecedor, String codigoAcesso) {
        fornecedorService.verificaFornecedor(idFornecedor, codigoAcesso);

        Cafe cafe = cafeRepository.findById(idCafe).orElseThrow(CafeNaoExisteException::new);
        cafeRepository.delete(cafe);
    }

    @Override
    public CafeResponseDTO recuperar(Long idCafe) {
        Cafe cafe = cafeRepository.findById(idCafe).orElseThrow(CafeNaoExisteException::new);
        return new CafeResponseDTO(cafe);
    }

    @Override
    public Cafe recuperaCafe(Long id) {
        return cafeRepository.findById(id).orElseThrow(CafeNaoExisteException::new);
    }

    @Override
    public CafeResponseDTO alterar(Long idFornecedor, String codigoAcesso, Long idCafe, CafePostPutRequestDTO cafePostPutRequestDTO) {
        fornecedorService.verificaFornecedor(idFornecedor, codigoAcesso);

        Cafe cafe = cafeRepository.findById(idCafe).orElseThrow(CafeNaoExisteException::new);
        modelMapper.map(cafePostPutRequestDTO, cafe);
        cafeRepository.save(cafe);
        return modelMapper.map(cafe, CafeResponseDTO.class);
    }

    @Override
    public List<CafeResponseDTO> listar() {
        List<Cafe> cafes = cafeRepository.findAll();
        return cafes.stream()
                .map(CafeResponseDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<CafeResponseDTO> listarPorFornecedor(Long idFornecedor) {
        Fornecedor fornecedor = fornecedorService.verificaFornecedor(idFornecedor);
        List<Cafe> cafes = cafeRepository.findByFornecedor(fornecedor);
        return cafes.stream()
                .map(CafeResponseDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<CafeResponseDTO> listarFiltraQualidade(Long idCliente) {
        ClienteResponseDTO cliente = clienteService.recuperar(idCliente);

        List<Cafe> cafes;

        if(cliente.getAssinatura().equals(TipoAssinatura.PREMIUM)) {
            cafes = cafeRepository.findByDisponivel(true);
        } else {
            cafes = cafeRepository.findByQualidadeAndDisponivel(QualidadeCafe.NORMAL, true);
        }


        return cafes.stream()
                .map(CafeResponseDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<CafeResponseDTO> listarFiltro(Long idCliente, String tipo, String origem, String perfil) {
        ClienteResponseDTO cliente = clienteService.recuperar(idCliente);

        TipoGraoCafe tipoGrao = null;

        if(!tipo.isBlank()) {
            tipoGrao = TipoGraoCafe.fromString(tipo);
        }

        if(cliente.getAssinatura().equals(TipoAssinatura.PREMIUM)) {
            return listarFiltroPremium(tipoGrao, origem, perfil);
        } else {
            return listarFiltroNormal(tipoGrao, origem, perfil);
        }
    }

    private List<CafeResponseDTO> listarFiltroPremium(TipoGraoCafe tipo, String origem, String perfil) {
        List<Cafe> cafes;

        if(tipo == null && origem.isBlank() && perfil.isBlank()) {
            cafes = cafeRepository.findByDisponivel(true);
        } else if(origem.isBlank() && perfil.isBlank()) {
            cafes = cafeRepository.findByTipoAndDisponivel(tipo, true);
        } else if(tipo == null && origem.isBlank()) {
            cafes = cafeRepository.findByDisponivelAndPerfilContainingIgnoreCase(true, perfil);
        } else if(tipo == null && perfil.isBlank()) {
            cafes = cafeRepository.findByDisponivelAndOrigemContainingIgnoreCase(true, origem);
        } else if(tipo == null) {
            cafes = cafeRepository.findByDisponivelAndOrigemContainingIgnoreCaseAndPerfilContainingIgnoreCase(true, origem, perfil);
        } else if(origem.isBlank()) {
            cafes = cafeRepository.findByDisponivelAndPerfilContainingIgnoreCaseAndTipo(true, perfil, tipo);
        } else if(perfil.isBlank()){
            cafes = cafeRepository.findByDisponivelAndOrigemContainingIgnoreCaseAndTipo(true, perfil, tipo);
        } else {
            cafes = cafeRepository.findByDisponivelAndOrigemContainingIgnoreCaseAndPerfilContainingIgnoreCaseAndTipo(true, origem, perfil, tipo);
        }

        return cafes.stream()
                .map(CafeResponseDTO::new)
                .collect(Collectors.toList());
    }

    private List<CafeResponseDTO> listarFiltroNormal(TipoGraoCafe tipo, String origem, String perfil) {
        List<Cafe> cafes;

        if(tipo == null && origem.isBlank() && perfil.isBlank()) {
            cafes = cafeRepository.findByQualidadeAndDisponivel(QualidadeCafe.NORMAL, true);
        } else if(origem.isBlank() && perfil.isBlank()) {
            cafes = cafeRepository.findByQualidadeAndTipoAndDisponivel(QualidadeCafe.NORMAL, tipo, true);
        } else if(tipo == null && origem.isBlank()) {
            cafes = cafeRepository.findByQualidadeAndDisponivelAndPerfilContainingIgnoreCase(QualidadeCafe.NORMAL, true, perfil);
        } else if(tipo == null && perfil.isBlank()) {
            cafes = cafeRepository.findByQualidadeAndDisponivelAndOrigemContainingIgnoreCase(QualidadeCafe.NORMAL ,true, origem);
        } else if(tipo == null) {
            cafes = cafeRepository.findByQualidadeAndDisponivelAndOrigemContainingIgnoreCaseAndPerfilContainingIgnoreCase(QualidadeCafe.NORMAL, true, origem, perfil);
        } else if(origem.isBlank()) {
            cafes = cafeRepository.findByQualidadeAndDisponivelAndPerfilContainingIgnoreCaseAndTipo(QualidadeCafe.NORMAL, true, perfil, tipo);
        } else if(perfil.isBlank()){
            cafes = cafeRepository.findByQualidadeAndDisponivelAndOrigemContainingIgnoreCaseAndTipo(QualidadeCafe.NORMAL, true, perfil, tipo);
        } else {
            cafes = cafeRepository.findByQualidadeAndDisponivelAndOrigemContainingIgnoreCaseAndPerfilContainingIgnoreCaseAndTipo(QualidadeCafe.NORMAL, true, origem, perfil, tipo);
        }

        return cafes.stream()
                .map(CafeResponseDTO::new)
                .collect(Collectors.toList());
    }
}