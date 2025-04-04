package com.ufcg.psoft.commerce.service.cafe;

import com.ufcg.psoft.commerce.dto.cliente.ClientePostPutRequestDTO;
import com.ufcg.psoft.commerce.exception.CafeNaoEInteresseDeClienteException;
import com.ufcg.psoft.commerce.exception.CafeNaoExisteException;
import com.ufcg.psoft.commerce.exception.FornecedorNaoForneceCafeException;
import com.ufcg.psoft.commerce.exception.InteresseEmCafeDisponivelException;
import com.ufcg.psoft.commerce.model.CafeSpecification;
import com.ufcg.psoft.commerce.model.Cliente;
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
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Collections;
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
        Cafe cafe = verificaCafe(idCafe, idFornecedor, codigoAcesso);
        cafeRepository.delete(cafe);
    }

    @Override
    public CafeResponseDTO recuperar(Long idCafe) {
        Cafe cafe = cafeRepository.findById(idCafe).orElseThrow(CafeNaoExisteException::new);
        return new CafeResponseDTO(cafe);
    }

    @Override
    public CafeResponseDTO alterar(Long idFornecedor, String codigoAcesso, Long idCafe, CafePostPutRequestDTO cafePostPutRequestDTO) {
        Cafe cafe = verificaCafe(idCafe, idFornecedor, codigoAcesso);
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

    private List<Cafe> ordenaPorDisponibilidade(List<Cafe> cafes){
        Collections.sort(cafes, (c1, c2) -> {
            return Boolean.compare(c2.isDisponivel(), c1.isDisponivel());
        });
        return cafes;
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
        Specification<Cafe> spec = Specification
                .where(CafeSpecification.hasQualidade(null)
                        .and(CafeSpecification.hasOrigem(origem))
                        .and(CafeSpecification.hasTipo(tipo))
                        .and(CafeSpecification.hasPerfil(perfil)));

        List<Cafe> cafes = cafeRepository.findAll(spec);

        cafes = ordenaPorDisponibilidade(cafes);

        return cafes.stream()
                .map(CafeResponseDTO::new)
                .collect(Collectors.toList());
    }

    private List<CafeResponseDTO> listarFiltroNormal(TipoGraoCafe tipo, String origem, String perfil) {

        Specification<Cafe> spec = Specification
                .where(CafeSpecification.hasQualidade(QualidadeCafe.NORMAL)
                        .and(CafeSpecification.hasOrigem(origem))
                        .and(CafeSpecification.hasTipo(tipo))
                        .and(CafeSpecification.hasPerfil(perfil)));

        List<Cafe> cafes = cafeRepository.findAll(spec);

        cafes = ordenaPorDisponibilidade(cafes);

        return cafes.stream()
                .map(CafeResponseDTO::new)
                .collect(Collectors.toList());
    }


    @Override
    public ClienteResponseDTO demonstrarInteresse(Long idCliente, String codigoAcesso, Long idCafe){
        Cliente cliente = clienteService.verificaCliente(idCliente, codigoAcesso);
        Cafe cafe = recuperaCafe(idCafe);

        if (cafe.isDisponivel()) {
            throw new InteresseEmCafeDisponivelException();
        }

        if (!(cliente.getCafesDeInteresse().contains(cafe))) {
            cliente.getCafesDeInteresse().add(cafe);
            clienteService.alterar(idCliente,codigoAcesso,modelMapper.map(cliente, ClientePostPutRequestDTO.class));
        }

        return new ClienteResponseDTO(cliente);

    }

    @Override
    public List<CafeResponseDTO> listarCafesInteresseCliente(Long idCliente, String codigoAcesso) {
        Cliente cliente = clienteService.verificaCliente(idCliente,codigoAcesso);

        List<Cafe> cafes = cliente.getCafesDeInteresse();
        return cafes.stream().map(CafeResponseDTO::new).collect(Collectors.toList());
    }


    @Override
    public CafeResponseDTO alterarDisponibilidadeCafe(Long idCafe, Long idFornecedor, String codigoAcesso, boolean disponibilidade){
        Cafe cafe = verificaCafe(idCafe, idFornecedor, codigoAcesso);
        if(disponibilidade && !cafe.isDisponivel()){
            notificaClientesInteressados(cafe);
        }
        cafe.setDisponivel(disponibilidade);
        cafeRepository.save(cafe);
        return new CafeResponseDTO(cafe);
    }
    @Override
    public void removerInteresseClienteCafe(Long idCliente, String codigoAcesso, Long idCafe) {
        Cliente cliente = clienteService.verificaCliente(idCliente, codigoAcesso);
        Cafe cafe = recuperaCafe(idCafe);

        if(!(cliente.getCafesDeInteresse().contains(cafe))){
            throw new CafeNaoEInteresseDeClienteException();
        }

        cliente.getCafesDeInteresse().remove(cafe);
    }

    private void notificaClientesInteressados(Cafe cafe) {
        List<Cliente> clientes = cafe.getClientesInteressados().stream().sorted((c1, c2) -> {
            return c1.getAssinatura().compareTo(c2.getAssinatura());
        }).toList();

        for (Cliente cliente : clientes) {
            cliente.notificaCafeDisponivel(cafe);
        }
    }

    @Override
    public Cafe recuperaCafe(Long id) {
        return cafeRepository.findById(id).orElseThrow(CafeNaoExisteException::new);
    }

    private Cafe verificaCafe(Long id, Long idFornecedor, String codigoAcesso) {
        fornecedorService.verificaFornecedor(idFornecedor, codigoAcesso);
        Cafe cafe = recuperaCafe(id);
        if (cafe.getFornecedor().getId() != idFornecedor){
            throw new FornecedorNaoForneceCafeException();
        }
        return cafe;
    }
}