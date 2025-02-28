package com.ufcg.psoft.commerce.service.pedido;

import com.ufcg.psoft.commerce.dto.pedido.PedidoPostPutRequestDTO;
import com.ufcg.psoft.commerce.dto.pedido.PedidoResponseDTO;
import com.ufcg.psoft.commerce.exception.ClienteInvalido;
import com.ufcg.psoft.commerce.exception.PedidoNaoExiste;
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

        Pedido pedido = pedidoRepository.save(Pedido.builder()
                .cliente(cliente)
                .endereco(pedidoPostPutRequestDTO.getEndereco() != null ? modelMapper.map(pedidoPostPutRequestDTO.getEndereco(), Endereco.class) : cliente.getEndereco())
                .cafe(cafe)
                .assinatura(cliente.getAssinatura())
                .fornecedor(cafe.getFornecedor())
                .build()
        );

        return modelMapper.map(pedido, PedidoResponseDTO.class);
    }

    @Override
    public PedidoResponseDTO alterar(Long idCliente, String codigo, Long idPedido, PedidoPostPutRequestDTO pedidoPostPutRequestDTO) {
        Pedido pedido = pedidoRepository.findById(idPedido).orElseThrow(PedidoNaoExiste::new);

        Cliente cliente = clienteService.verificaCliente(idCliente, codigo);

        Cafe cafe = cafeService.recuperaCafe(pedidoPostPutRequestDTO.getIdCafe());

        Endereco endereco = modelMapper.map(pedidoPostPutRequestDTO.getEndereco(), Endereco.class);

        pedido.setCafe(cafe);
        pedido.setEndereco(endereco);
        pedido.setFornecedor(cafe.getFornecedor());
        pedidoRepository.save(pedido);

        return modelMapper.map(pedido, PedidoResponseDTO.class);
    }

    @Override
    public void remover(Long idCliente, String codigoCliente, Long idPedido) {
        Pedido pedido = pedidoRepository.findById(idPedido).orElseThrow(PedidoNaoExiste::new);

        if(!pedido.getCliente().getId().equals(idCliente)) {
            throw new ClienteInvalido();
        } else {
            clienteService.verificaCliente(idCliente, codigoCliente);

            pedidoRepository.delete(pedido);
        }
    }

    @Override
    public List<PedidoResponseDTO> listarPedidoCliente(Long idCliente, String codigoCliente) {
        Cliente cliente = clienteService.verificaCliente(idCliente, codigoCliente);

        List<Pedido> pedidos = pedidoRepository.findByCliente(cliente);

        return pedidos.stream()
                .map(PedidoResponseDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<PedidoResponseDTO> listarPedidoFornecedor(Long idFornecedor, String codigoFornecedor) {
        Fornecedor fornecedor = fornecedorService.verificaFornecedor(idFornecedor, codigoFornecedor);

        List<Pedido> pedidos = pedidoRepository.findByFornecedor(fornecedor);

        return pedidos.stream()
                .map(PedidoResponseDTO::new)
                .collect(Collectors.toList());
    }
}