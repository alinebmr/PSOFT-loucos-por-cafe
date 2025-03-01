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

    @GetMapping("/{id}")
    public ResponseEntity<?> listar(
            @PathVariable Long id,
            @RequestParam String codigoAcesso,
            @RequestParam boolean isFornecedor) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(pedidoService.listar(id, codigoAcesso, isFornecedor));
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
            @RequestParam Long id,
            @RequestParam String codigo,
            @RequestParam boolean isFornecedor,
            @RequestBody @Valid PedidoPostPutRequestDTO pedidoPostPutRequestDTO) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(pedidoService.alterar(id, codigo, idPedido, pedidoPostPutRequestDTO, isFornecedor));
    }

    @PatchMapping("/{idPedido}/pagar")
    public ResponseEntity<?> confirmarPagamento(
            @PathVariable Long idPedido,
            @RequestParam Long idCliente,
            @RequestParam String codigoAcesso) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(pedidoService.confirmarPagamento(idPedido, idCliente, codigoAcesso));
    }

    @DeleteMapping("/{idPedido}")
    public ResponseEntity<?> excluirPedido(
            @PathVariable Long idPedido,
            @RequestParam Long id,
            @RequestParam String codigo,
            @RequestParam boolean isFornecedor) {
        pedidoService.remover(id, codigo, idPedido, isFornecedor);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body("");
    }
}