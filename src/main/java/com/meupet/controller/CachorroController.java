package com.meupet.controller;

import com.meupet.dto.CachorroRequestDTO;
import com.meupet.dto.CachorroResponseDTO;
import com.meupet.service.CachorroService;
import com.meupet.model.DadoInvalidoException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cachorros") // endpoint base para cachorros
@Tag(name = "Cachorros", description = "Endpoints para gerenciamento de cachorros")
public class CachorroController {

    private final CachorroService service;

    public CachorroController(CachorroService service) {
        this.service = service;
    }

    // POST → cadastrar cachorro
    @PostMapping
    @Operation(summary = "Cadastrar um novo cachorro")
    public ResponseEntity<CachorroResponseDTO> cadastrar(
            @Valid @RequestBody CachorroRequestDTO request
    ) throws DadoInvalidoException {

        CachorroResponseDTO novo = service.criar(request);
        return new ResponseEntity<>(novo, HttpStatus.CREATED);
    }

    // GET → listar todos
    @GetMapping
    @Operation(summary = "Listar todos os cachorros")
    public ResponseEntity<List<CachorroResponseDTO>> listar() {

        List<CachorroResponseDTO> lista = service.listarTodos();
        return ResponseEntity.ok(lista);
    }
}