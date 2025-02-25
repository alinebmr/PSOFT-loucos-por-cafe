package com.ufcg.psoft.commerce.controller;

import com.ufcg.psoft.commerce.dto.cafe.CafePostPutRequestDTO;
import com.ufcg.psoft.commerce.service.cafe.CafeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(
    value = "/cafes",
    produces = MediaType.APPLICATION_JSON_VALUE
)

public class CafeController {
    
    @Autowired
    CafeService cafeService;

        @PostMapping()
    public ResponseEntity<?> criarCafe(
            @RequestParam Long idFornecedor,
            @RequestParam String codigoAcesso,
            @RequestBody @Valid CafePostPutRequestDTO cafePostPutRequestDto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(cafeService.criar(idFornecedor, codigoAcesso, cafePostPutRequestDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluirCafe(
            @PathVariable Long idCafe,
            @RequestParam Long idFornecedor,
            @RequestParam String codigo) {
        cafeService.remover(idCafe, idFornecedor, codigo);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body("");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarCafe(
            @PathVariable Long id,
            @RequestParam Long idFornecedor,
            @RequestParam String codigo,
            @RequestBody @Valid CafePostPutRequestDTO cafePostPutRequestDto) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(cafeService.alterar(idFornecedor, codigo, id, cafePostPutRequestDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> recuperarCafe(
            @PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(cafeService.recuperar(id));
    }

    @GetMapping("")
    public ResponseEntity<?> listarCafePorFornecedores(
            @RequestParam(required = false, defaultValue = "") Long idFornecedor, 
            @RequestParam(required = false, defaultValue = "") String codigoAcesso) {

        if (idFornecedor != null && idFornecedor != 0) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(cafeService.listarPorFornecedor(idFornecedor, codigoAcesso));
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(cafeService.listar());
    }
}
