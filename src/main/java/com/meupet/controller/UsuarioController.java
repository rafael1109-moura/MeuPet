package com.meupet.controller;

import com.meupet.dto.UsuarioRequestDTO;
import com.meupet.dto.UsuarioResponseDTO;
import com.meupet.model.DadoInvalidoException;
import com.meupet.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
@Tag(name = "Usuários", description = "Endpoints para gerenciamento de usuários")
public class UsuarioController {

    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Cadastrar um novo usuário")
    public ResponseEntity<UsuarioResponseDTO> cadastrar(@Valid @RequestBody UsuarioRequestDTO request)
            throws DadoInvalidoException {
        return new ResponseEntity<>(service.criar(request), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Listar todos os usuários cadastrados")
    public ResponseEntity<Page<UsuarioResponseDTO>> listar(
            @PageableDefault(size = 10, sort = "id") Pageable pageable) {
        return ResponseEntity.ok(service.listarTodas(pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar usuário por id")
    public ResponseEntity<UsuarioResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar um usuário")
    public ResponseEntity<UsuarioResponseDTO> atualizar(@PathVariable Long id,
            @Valid @RequestBody UsuarioRequestDTO request) throws DadoInvalidoException {
        return ResponseEntity.ok(service.atualizar(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir um usuário")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}