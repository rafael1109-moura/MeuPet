package com.meupet.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.meupet.dto.GatoRequestDTO;
import com.meupet.dto.GatoResponseDTO;
import com.meupet.service.GatoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

// Controller REST para gatos.
@RestController
@RequestMapping("/api/gatos")
@Tag(name = "Gatos", description = "Endpoints para gerenciamento de gatos")
public class GatoController {

    private final GatoService service;

    public GatoController(GatoService service) {
        this.service = service;
    }

    // Cadastra um novo gato.
    @PostMapping
    @Operation(summary = "Cadastrar um novo gato")
    public ResponseEntity<GatoResponseDTO> cadastrar(@Valid @RequestBody GatoRequestDTO request) {
        GatoResponseDTO novo = service.criar(request);
        return new ResponseEntity<>(novo, HttpStatus.CREATED);
    }

    // Lista todos os gatos.
    @GetMapping
    @Operation(summary = "Listar todos os gatos")
    public ResponseEntity<List<GatoResponseDTO>> listar() {
        return ResponseEntity.ok(service.listarTodos());
    }
}
