package com.meupet.controller;

import com.meupet.dto.CachorroRequestDTO;
import com.meupet.dto.CachorroResponseDTO;
import com.meupet.model.DadoInvalidoException;
import com.meupet.service.CachorroService;

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
@RequestMapping("/api/cachorros")
@Tag(name = "Cachorros", description = "Endpoints para gerenciamento de cachorros")
public class CachorroController {

    private final CachorroService service;

    public CachorroController(CachorroService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Cadastrar um novo cachorro")
    public ResponseEntity<CachorroResponseDTO> cadastrar(@Valid @RequestBody CachorroRequestDTO request)
            throws DadoInvalidoException {
        return new ResponseEntity<>(service.criar(request), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Listar todos os cachorros")
    public ResponseEntity<Page<CachorroResponseDTO>> listar(
            @PageableDefault(size = 10, sort = "id") Pageable pageable) {
        return ResponseEntity.ok(service.listarTodos(pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar cachorro por id")
    public ResponseEntity<CachorroResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar um cachorro")
    public ResponseEntity<CachorroResponseDTO> atualizar(@PathVariable Long id,
            @Valid @RequestBody CachorroRequestDTO request) throws DadoInvalidoException {
        return ResponseEntity.ok(service.atualizar(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir um cachorro")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}