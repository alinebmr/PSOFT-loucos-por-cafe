package com.ufcg.psoft.commerce.service.cafe;

import com.ufcg.psoft.commerce.exception.CafeNaoExisteException;
import com.ufcg.psoft.commerce.service.fornecedor.FornecedorService;
import com.ufcg.psoft.commerce.repository.CafeRepository;
import com.ufcg.psoft.commerce.dto.cafe.CafePostPutRequestDTO;
import com.ufcg.psoft.commerce.dto.cafe.CafeResponseDTO;
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
}
