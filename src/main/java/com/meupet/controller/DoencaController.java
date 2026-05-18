/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.meupet.controller;

/**
 *
 * @author edvaldinhs
 */
import com.meupet.dto.DoencaRequestDTO;
import com.meupet.dto.DoencaResponseDTO;
import com.meupet.model.DadoInvalidoException;
import com.meupet.service.DoencaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doencas")
@Tag(name = "Doenças", description = "Endpoints para gerenciamento de doenças")
public class DoencaController {

    private final DoencaService service;

    public DoencaController(DoencaService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Cadastrar uma nova doença")
    public ResponseEntity<DoencaResponseDTO> cadastrar(@Valid @RequestBody DoencaRequestDTO request) throws DadoInvalidoException {
        DoencaResponseDTO novaDoenca = service.criar(request);
        return new ResponseEntity<>(novaDoenca, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Listar todas as doenças cadastradas")
    public ResponseEntity<List<DoencaResponseDTO>> listar() {
        List<DoencaResponseDTO> lista = service.listarTodas();
        return ResponseEntity.ok(lista);
    }
}