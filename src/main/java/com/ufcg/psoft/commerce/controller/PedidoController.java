package com.ufcg.psoft.commerce.controller;

import com.ufcg.psoft.commerce.dto.pedido.PedidoPostPutRequestDTO;
import com.ufcg.psoft.commerce.service.pedido.PedidoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(
        value = "/pedidos",
        produces = MediaType.APPLICATION_JSON_VALUE
)

public class PedidoController {

    @Autowired
    PedidoService pedidoService;

    @GetMapping("/{idCliente}")
    public ResponseEntity<?> listarPorCliente(
            @PathVariable Long idCliente,
            @RequestParam String codigoAcesso) {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(pedidoService.listarPedidoCliente(idCliente, codigoAcesso));
    }

    @GetMapping("/{idFornecedor}")
    public ResponseEntity<?> listarPorFornecedor(
            @PathVariable Long idFornecedor,
            @RequestParam String codigoAcesso) {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(pedidoService.listarPedidoFornecedor(idFornecedor, codigoAcesso));
    }

    @PostMapping()
    public ResponseEntity<?> criarPedido(
            @RequestParam Long idCliente,
            @RequestParam String codigoAcesso,
            @RequestBody @Valid PedidoPostPutRequestDTO pedidoPostPutRequestDTO) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(pedidoService.criar(idCliente, codigoAcesso, pedidoPostPutRequestDTO));
    }

    @PutMapping("/{idPedido}")
    public ResponseEntity<?> atualizarPedido(
            @PathVariable Long idPedido,
            @RequestParam Long idCliente,
            @RequestParam String codigoCliente,
            @RequestBody @Valid PedidoPostPutRequestDTO pedidoPostPutRequestDTO) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(pedidoService.alterar(idCliente, codigoCliente, idPedido, pedidoPostPutRequestDTO));
    }

    @DeleteMapping("/{idPedido}")
    public ResponseEntity<?> excluirPedido(
            @PathVariable Long idPedido,
            @RequestParam Long idCliente,
            @RequestParam String codigoCliente) {
        pedidoService.remover(idCliente, codigoCliente, idPedido);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body("");
    }
}