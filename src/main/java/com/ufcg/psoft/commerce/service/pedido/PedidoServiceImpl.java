package com.ufcg.psoft.commerce.service.pedido;

import com.ufcg.psoft.commerce.dto.pedido.PedidoPostPutRequestDTO;
import com.ufcg.psoft.commerce.dto.pedido.PedidoResponseDTO;
import com.ufcg.psoft.commerce.enums.QualidadeCafe;
import com.ufcg.psoft.commerce.enums.TipoAssinatura;
import com.ufcg.psoft.commerce.enums.TipoPagamento;
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

        Pedido pedido = Pedido.builder()
            .cafe(cafe)
            .cliente(cliente)
            .assinatura(cliente.getAssinatura())
            .build();

        modelMapper.map(pedidoPostPutRequestDTO, pedido);

        if (pedido.getEndereco() == null) {
            pedido.setEndereco(cliente.getEndereco());
        }

        pedido = pedidoRepository.save(pedido);

        return new PedidoResponseDTO(pedido);
    }

    @Override
    public PedidoResponseDTO alterar(Long id, String codigo, Long idPedido, PedidoPostPutRequestDTO pedidoPostPutRequestDTO, boolean isFornecedor) {
        Pedido pedido = verificaPedido(idPedido, id, codigo, isFornecedor);

        Cafe cafe = cafeService.recuperaCafe(pedidoPostPutRequestDTO.getIdCafe());

        verificaDisponibilidadeCafe(cafe.isDisponivel());
        verificaQualidadeAssinatura(cafe.getQualidade(), pedido.getAssinatura());

        modelMapper.map(pedidoPostPutRequestDTO, pedido);
        pedido.setCafe(cafe);

        pedido = pedidoRepository.save(pedido);

        return new PedidoResponseDTO(pedido);
    }

    @Override
    public void remover(Long id, String codigo, Long idPedido, boolean isFornecedor) {
        Pedido pedido = verificaPedido(idPedido, id, codigo, isFornecedor);

        pedidoRepository.delete(pedido);
    }

    @Override
    public List<PedidoResponseDTO> listar(Long id, String codigo, boolean isFornecedor) {
        List<Pedido> pedidos;

        if(isFornecedor) {
            Fornecedor fornecedor = fornecedorService.verificaFornecedor(id, codigo);

            pedidos = pedidoRepository.findByCafeFornecedor(fornecedor);
        } else {
            Cliente cliente = clienteService.verificaCliente(id, codigo);

            pedidos = pedidoRepository.findByCliente(cliente);
        }

        return pedidos.stream()
            .map(PedidoResponseDTO::new)
            .collect(Collectors.toList());
    }

    @Override
    public PedidoResponseDTO recuperar(Long id, Long idUsuario, String codigo, boolean isFornecedor) {
        Pedido pedido = verificaPedido(id, idUsuario, codigo, isFornecedor);

        return new PedidoResponseDTO(pedido);
    }

    @Override
    public PedidoResponseDTO confirmarPagamento(Long idPedido, Long idCliente, String codigoAcesso) {
        Pedido pedido = verificaPedido(idPedido, idCliente, codigoAcesso, false);

        if (pedido.isPago()) {
            throw new CommerceException("Pedido ja foi pago!");
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

    private Pedido verificaPedido(Long idPedido, Long idUsuario, String codigo, boolean isFornecedor) {
        Pedido pedido = pedidoRepository.findById(idPedido).orElseThrow(PedidoNaoExisteException::new);

        if (isFornecedor) {
            if (!pedido.getCafe().getFornecedor().getId().equals(idUsuario)) {
                throw new FornecedorInvalidoException();
            } else {
                fornecedorService.verificaFornecedor(idUsuario, codigo);
            }
        } else {
            if (!pedido.getCliente().getId().equals(idUsuario)) {
                throw new ClienteInvalidoException();
            } else {
                clienteService.verificaCliente(idUsuario, codigo);
            }
        }

        return pedido;
    }
}