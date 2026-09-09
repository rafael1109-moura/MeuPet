package com.meupet.controller;

import com.meupet.dto.GatoRequestDTO;
import com.meupet.dto.GatoResponseDTO;
import com.meupet.service.GatoService;

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
@RequestMapping("/api/gatos")
@Tag(name = "Gatos", description = "Endpoints para gerenciamento de gatos")
public class GatoController {

    private final GatoService service;

    public GatoController(GatoService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Cadastrar um novo gato")
    public ResponseEntity<GatoResponseDTO> cadastrar(@Valid @RequestBody GatoRequestDTO request) {
        return new ResponseEntity<>(service.criar(request), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Listar todos os gatos")
    public ResponseEntity<Page<GatoResponseDTO>> listar(
            @PageableDefault(size = 10, sort = "id") Pageable pageable) {
        return ResponseEntity.ok(service.listarTodos(pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar gato por id")
    public ResponseEntity<GatoResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar um gato")
    public ResponseEntity<GatoResponseDTO> atualizar(@PathVariable Long id,
            @Valid @RequestBody GatoRequestDTO request) {
        return ResponseEntity.ok(service.atualizar(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir um gato")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}