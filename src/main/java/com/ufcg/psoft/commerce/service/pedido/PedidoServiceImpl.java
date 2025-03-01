package com.ufcg.psoft.commerce.service.pedido;

import com.ufcg.psoft.commerce.dto.pedido.PedidoPostPutRequestDTO;
import com.ufcg.psoft.commerce.dto.pedido.PedidoResponseDTO;
import com.ufcg.psoft.commerce.enums.QualidadeCafe;
import com.ufcg.psoft.commerce.enums.TipoAssinatura;
import com.ufcg.psoft.commerce.exception.*;
import com.ufcg.psoft.commerce.model.*;
import com.ufcg.psoft.commerce.repository.PedidoRepository;
import com.ufcg.psoft.commerce.service.cafe.CafeService;
import com.ufcg.psoft.commerce.service.cliente.ClienteService;
import com.ufcg.psoft.commerce.service.fornecedor.FornecedorService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PedidoServiceImpl implements PedidoService {

    @Autowired
    PedidoRepository pedidoRepository;
    @Autowired
    FornecedorService fornecedorService;
    @Autowired
    ClienteService clienteService;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    CafeService cafeService;

    @Override
    public PedidoResponseDTO criar(Long idCliente, String codigoCliente, PedidoPostPutRequestDTO pedidoPostPutRequestDTO) {
        Cliente cliente = clienteService.verificaCliente(idCliente, codigoCliente);
        Cafe cafe = cafeService.recuperaCafe(pedidoPostPutRequestDTO.getIdCafe());

        verificaDisponibilidadeCafe(cafe.isDisponivel());
        verificaQualidadeAssinatura(cafe.getQualidade(), cliente.getAssinatura());

        Pedido pedido = pedidoRepository.save(Pedido.builder()
                .cliente(cliente)
                .endereco(pedidoPostPutRequestDTO.getEndereco() != null ? modelMapper.map(pedidoPostPutRequestDTO.getEndereco(), Endereco.class) : cliente.getEndereco())
                .cafe(cafe)
                .assinatura(cliente.getAssinatura())
                .build()
        );

        return modelMapper.map(pedido, PedidoResponseDTO.class);
    }

    @Override
    public PedidoResponseDTO alterar(Long id, String codigo, Long idPedido, PedidoPostPutRequestDTO pedidoPostPutRequestDTO, boolean isFornecedor) {
        Pedido pedido = pedidoRepository.findById(idPedido).orElseThrow(PedidoNaoExisteException::new);

        if(isFornecedor) {
            if(!pedido.getCafe().getFornecedor().getId().equals(id)) {
                throw new FornecedorInvalidoException();
            } else {
                fornecedorService.verificaFornecedor(id, codigo);
            }
        } else {
            if(!pedido.getCliente().getId().equals(id)) {
                throw new ClienteInvalidoException();
            } else {
                clienteService.verificaCliente(id, codigo);
            }
        }

        Cafe cafe = cafeService.recuperaCafe(pedidoPostPutRequestDTO.getIdCafe());

        verificaDisponibilidadeCafe(cafe.isDisponivel());
        verificaQualidadeAssinatura(cafe.getQualidade(), pedido.getAssinatura());

        if(pedidoPostPutRequestDTO.getEndereco() != null) {
            Endereco endereco = modelMapper.map(pedidoPostPutRequestDTO.getEndereco(), Endereco.class);
            pedido.setEndereco(endereco);
        }

        pedido.setCafe(cafe);
        pedidoRepository.save(pedido);

        return modelMapper.map(pedido, PedidoResponseDTO.class);
    }

    @Override
    public void remover(Long id, String codigo, Long idPedido, boolean isFornecedor) {
        Pedido pedido = pedidoRepository.findById(idPedido).orElseThrow(PedidoNaoExisteException::new);

        if(isFornecedor) {
            if(!pedido.getCafe().getFornecedor().getId().equals(id)) {
                throw new FornecedorInvalidoException();
            } else {
                fornecedorService.verificaFornecedor(id, codigo);
            }
        } else {
            if(!pedido.getCliente().getId().equals(id)) {
                throw new ClienteInvalidoException();
            } else {
                clienteService.verificaCliente(id, codigo);
            }
        }

        pedidoRepository.delete(pedido);
    }

    @Override
    public List<PedidoResponseDTO> listar(Long id, String codigo, boolean isFornecedor) {
        List<Pedido> pedidos;

        if(isFornecedor) {
            Fornecedor fornecedor = fornecedorService.verificaFornecedor(id, codigo);

            pedidos = pedidoRepository.findByFornecedor(fornecedor);
        } else {
            Cliente cliente = clienteService.verificaCliente(id, codigo);

            pedidos = pedidoRepository.findByCliente(cliente);
        }

        return pedidos.stream()
                .map(PedidoResponseDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public PedidoResponseDTO confirmarPagamento(Long idPedido, Long idCliente, String codigoAcesso) {
        Pedido pedido = pedidoRepository.findById(idPedido).orElseThrow(PedidoNaoExisteException::new);

        clienteService.verificaCliente(idCliente, codigoAcesso);

        if (!pedido.getCliente().getId().equals(idCliente)) {
            throw new ClienteInvalidoException();
        }

        pedido.setPago(true);
        pedidoRepository.save(pedido);

        return new PedidoResponseDTO(pedido);
    }

    private void verificaQualidadeAssinatura(QualidadeCafe cafe, TipoAssinatura assinatura) {
        if(cafe.equals(QualidadeCafe.PREMIUM) && assinatura.equals(TipoAssinatura.NORMAL)) {
            throw new CommerceException("Assinatura invalida para este cafe!");
        }
    }

    private void verificaDisponibilidadeCafe(boolean disponibilidade) {
        if(!disponibilidade) {
            throw new CafeIndisponivelException();
        }
    }
}