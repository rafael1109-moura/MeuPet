package com.meupet.controller;

import com.meupet.dto.DoencaRequestDTO;
import com.meupet.dto.DoencaResponseDTO;
import com.meupet.model.DadoInvalidoException;
import com.meupet.service.DoencaService;
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
@RequestMapping("/api/doencas")
@Tag(name = "Doenças", description = "Endpoints para gerenciamento de doenças")
public class DoencaController {

    private final DoencaService service;

    public DoencaController(DoencaService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Cadastrar uma nova doença")
    public ResponseEntity<DoencaResponseDTO> cadastrar(@Valid @RequestBody DoencaRequestDTO request)
            throws DadoInvalidoException {
        return new ResponseEntity<>(service.criar(request), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Listar todas as doenças cadastradas")
    public ResponseEntity<Page<DoencaResponseDTO>> listar(
            @PageableDefault(size = 10, sort = "id") Pageable pageable) {
        return ResponseEntity.ok(service.listarTodas(pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar doença por id")
    public ResponseEntity<DoencaResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar uma doença")
    public ResponseEntity<DoencaResponseDTO> atualizar(@PathVariable Long id,
            @Valid @RequestBody DoencaRequestDTO request) throws DadoInvalidoException {
        return ResponseEntity.ok(service.atualizar(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir uma doença")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}