package com.meupet.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.meupet.dto.AnimalRequestDTO;
import com.meupet.dto.AnimalResponseDTO;
import com.meupet.service.AnimalService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

// Controller REST para o endpoint generico de animais.
@RestController
@RequestMapping("/api/animais")
@Tag(name = "Animais", description = "Endpoints para gerenciamento de animais")
public class AnimalController {

    private final AnimalService service;

    public AnimalController(AnimalService service) {
        this.service = service;
    }

    // Cadastra um animal sem expor a entidade diretamente.
    @PostMapping
    @Operation(summary = "Cadastrar um novo animal")
    public ResponseEntity<AnimalResponseDTO> cadastrar(@Valid @RequestBody AnimalRequestDTO request) {
        AnimalResponseDTO response = service.criar(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
