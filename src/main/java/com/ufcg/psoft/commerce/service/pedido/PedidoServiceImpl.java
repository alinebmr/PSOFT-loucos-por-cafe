package com.ufcg.psoft.commerce.service.pedido;

import com.ufcg.psoft.commerce.dto.pedido.PedidoPostPutRequestDTO;
import com.ufcg.psoft.commerce.dto.pedido.PedidoResponseDTO;
import com.ufcg.psoft.commerce.enums.QualidadeCafe;
import com.ufcg.psoft.commerce.enums.StatusPedidoEnum;
import com.ufcg.psoft.commerce.enums.TipoAssinatura;
import com.ufcg.psoft.commerce.enums.TipoPagamento;
import com.ufcg.psoft.commerce.exception.*;
import com.ufcg.psoft.commerce.model.*;
import com.ufcg.psoft.commerce.repository.PedidoRepository;
import com.ufcg.psoft.commerce.service.cafe.CafeService;
import com.ufcg.psoft.commerce.service.cliente.ClienteService;
import com.ufcg.psoft.commerce.service.entregador.EntregadorService;
import com.ufcg.psoft.commerce.service.fornecedor.FornecedorService;
import com.ufcg.psoft.commerce.service.util.InterService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    @Autowired
    EntregadorService entregadorService;
    @Autowired
    InterService interService;

    @Override
    public PedidoResponseDTO criar(Long idCliente, String codigoCliente, PedidoPostPutRequestDTO pedidoPostPutRequestDTO) {
        Cliente cliente = clienteService.verificaCliente(idCliente, codigoCliente);
        Cafe cafe = cafeService.recuperaCafe(pedidoPostPutRequestDTO.getIdCafe());

        verificaDisponibilidadeCafe(cafe.isDisponivel());
        verificaQualidadeAssinatura(cafe.getQualidade(), cliente.getAssinatura());
        verificaTipoPagamento(cafe.getFornecedor(), pedidoPostPutRequestDTO.getTipoPagamento());

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
        verificaTipoPagamento(cafe.getFornecedor(), pedidoPostPutRequestDTO.getTipoPagamento());

        modelMapper.map(pedidoPostPutRequestDTO, pedido);
        pedido.setCafe(cafe);

        pedido = pedidoRepository.save(pedido);

        return new PedidoResponseDTO(pedido);
    }

    @Override
    public void cancelarPedido(Long idPedido, Long idCliente, String codigoAcesso) {
        Pedido pedido = verificaPedido(idPedido, idCliente, codigoAcesso, false);

        if(pedido.getStatus().equals(StatusPedidoEnum.RECEBIDO) || pedido.getStatus().equals(StatusPedidoEnum.PREPARACAO)){
            pedidoRepository.delete(pedido);
        }else{
            throw new CommerceException("Pedido nao pode mais ser cancelado!");
        }
    }

    @Override
    public List<PedidoResponseDTO> listarPorStatus(Long id, String codigo,StatusPedidoEnum status){
        List<Pedido> pedidos;
        Cliente cliente = clienteService.verificaCliente(id, codigo);

        pedidos = pedidoRepository.findByStatusAndClienteOrderedByCreatedAt(status, cliente);

        return pedidos.stream()
        .map(PedidoResponseDTO::new)
        .collect(Collectors.toList());
    }

    @Override
    public List<PedidoResponseDTO> listar(Long id, String codigo, boolean isFornecedor) {
        List<Pedido> pedidos = new ArrayList<Pedido>();

        if(isFornecedor) {
            Fornecedor fornecedor = fornecedorService.verificaFornecedor(id, codigo);

            pedidos = pedidoRepository.findByCafeFornecedor(fornecedor);
        } else {
            Cliente cliente = clienteService.verificaCliente(id, codigo);

            pedidos.addAll(pedidoRepository.findByStatusAndClienteOrderedByCreatedAt(StatusPedidoEnum.RECEBIDO, cliente));
            pedidos.addAll(pedidoRepository.findByStatusAndClienteOrderedByCreatedAt(StatusPedidoEnum.PREPARACAO, cliente));
            pedidos.addAll(pedidoRepository.findByStatusAndClienteOrderedByCreatedAt(StatusPedidoEnum.PRONTO, cliente));
            pedidos.addAll(pedidoRepository.findByStatusAndClienteOrderedByCreatedAt(StatusPedidoEnum.EM_ENTREGA, cliente));
            pedidos.addAll(pedidoRepository.findByStatusAndClienteOrderedByCreatedAt(StatusPedidoEnum.ENTREGUE, cliente));

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
        pedido.nextState();
        pedidoRepository.save(pedido);

        return new PedidoResponseDTO(pedido);
    }

    @Override
    public PedidoResponseDTO pedidoPronto(Long idPedido, Long idFornecedor, String codigoAcesso) {
        Pedido pedido = verificaPedido(idPedido, idFornecedor, codigoAcesso, true);

        if(!pedido.getStatus().equals(StatusPedidoEnum.PREPARACAO)) {
            throw new StatusPedidoInvalidoException();
        }

        pedido.nextState();
        pedido = pedidoRepository.save(pedido);

        if(interService.atribuirEntregador(pedido)) {
            pedido = pedidoRepository.findById(pedido.getId()).orElseThrow(PedidoNaoExisteException::new);
        }else{
            pedido.getCliente().notificaPedidoNaoPodeSerAtribuidoParaEntrega(pedido);
        }

        return new PedidoResponseDTO(pedido);
    }

    @Override
    public PedidoResponseDTO confirmarEntrega(Long idPedido, Long idCliente, String codigoAcesso) {
        Pedido pedido = verificaPedido(idPedido, idCliente, codigoAcesso, false);

        if(!pedido.getStatus().equals(StatusPedidoEnum.EM_ENTREGA)) {
            throw new StatusPedidoInvalidoException();
        }

        pedido.nextState();
        
        Cafe cafePedido = pedido.getCafe();
        Fornecedor fornecedor = cafePedido.getFornecedor();
        pedido = pedidoRepository.save(pedido);
        entregadorService.atualizaUltimaEntrega(pedido.getEntregador().getId());
        fornecedor.notificaPedidoEntregue(pedido);

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

    private void verificaTipoPagamento(Fornecedor fornecedor, TipoPagamento tipoPagamento) {
        if (!fornecedor.getTiposPagamento().contains(tipoPagamento)) {
            throw new CommerceException("Fornecedor nao aceita esse tipo de pagamento!");
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