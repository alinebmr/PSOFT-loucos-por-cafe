package com.ufcg.psoft.commerce.controller;

import com.ufcg.psoft.commerce.dto.admin.AdminPostPutRequestDTO;
import com.ufcg.psoft.commerce.service.admin.AdminService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(
        value = "/admins",
        produces = MediaType.APPLICATION_JSON_VALUE
)
public class AdminController {
    
    @Autowired
    AdminService AdminService;

    @GetMapping("/{id}")
    public ResponseEntity<?> recuperarAdmin(
            @PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(AdminService.recuperar(id));
    }

    @GetMapping("")
    public ResponseEntity<?> listarAdmins(
            @RequestParam(required = false, defaultValue = "") String nome) {

        if (nome != null && !nome.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(AdminService.listarPorNome(nome));
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(AdminService.listar());
    }

    @PostMapping()
    public ResponseEntity<?> criarAdmin(
            @RequestBody @Valid AdminPostPutRequestDTO AdminPostPutRequestDto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(AdminService.criar(AdminPostPutRequestDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarAdmin(
            @PathVariable Long id,
            @RequestParam String codigo,
            @RequestBody @Valid AdminPostPutRequestDTO AdminPostPutRequestDto) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(AdminService.alterar(id, codigo, AdminPostPutRequestDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluirAdmin(
            @PathVariable Long id,
            @RequestParam String codigo) {
        AdminService.remover(id, codigo);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body("");
    }
}