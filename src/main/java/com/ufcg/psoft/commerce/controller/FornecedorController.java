package com.ufcg.psoft.commerce.controller;

import com.ufcg.psoft.commerce.dto.fornecedor.FornecedorPostPutRequestDTO;
import com.ufcg.psoft.commerce.service.fornecedor.FornecedorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(
    value = "/fornecedores",
    produces = MediaType.APPLICATION_JSON_VALUE
)
public class FornecedorController {
    
    @Autowired
    FornecedorService fornecedorService;

    @GetMapping("/{id}")
    public ResponseEntity<?> recuperarFornecedor(
            @PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(fornecedorService.recuperar(id));
    }

    @GetMapping("")
    public ResponseEntity<?> listarFornecedores(
            @RequestParam(required = false, defaultValue = "") String nomeEmpresa) {

        if (nomeEmpresa != null && !nomeEmpresa.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(fornecedorService.listarPorNome(nomeEmpresa));
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(fornecedorService.listar());
    }

    @PostMapping()
    public ResponseEntity<?> criarFornecedor(
            @RequestParam Long adminId,
            @RequestBody @Valid FornecedorPostPutRequestDTO fornecedorPostPutRequestDto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(fornecedorService.criar(adminId, fornecedorPostPutRequestDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarFornecedor(
            @PathVariable Long id,
            @RequestParam String codigo,
            @RequestBody @Valid FornecedorPostPutRequestDTO fornecedorPostPutRequestDto) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(fornecedorService.alterar(id, codigo, fornecedorPostPutRequestDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluirFornecedor(
            @PathVariable Long id,
            @RequestParam String codigo) {
        fornecedorService.remover(id, codigo);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body("");
    }
}
