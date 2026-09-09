package com.meupet.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.meupet.dto.AnimalRequestDTO;
import com.meupet.dto.AnimalResponseDTO;
import com.meupet.dto.TarefaResponseDTO;
import com.meupet.service.AnimalService;
import com.meupet.service.TarefaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/animais")
@Tag(name = "Animais", description = "Endpoints para gerenciamento de animais")
public class AnimalController {

    private final AnimalService service;
    private final TarefaService tarefaService;

    public AnimalController(AnimalService service, TarefaService tarefaService) {
        this.service = service;
        this.tarefaService = tarefaService;
    }

    @PostMapping
    @Operation(summary = "Cadastrar um novo animal")
    public ResponseEntity<AnimalResponseDTO> cadastrar(@Valid @RequestBody AnimalRequestDTO request) {
        return new ResponseEntity<>(service.criar(request), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Listar todos os animais")
    public ResponseEntity<Page<AnimalResponseDTO>> listar(
            @PageableDefault(size = 10, sort = "id") Pageable pageable) {
        return ResponseEntity.ok(service.listarTodos(pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar animal por id")
    public ResponseEntity<AnimalResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @GetMapping("/{id}/tarefas")
    @Operation(summary = "Listar tarefas de um animal")
    public ResponseEntity<Page<TarefaResponseDTO>> listarTarefas(@PathVariable Long id,
            @PageableDefault(size = 10, sort = "dataPrevista") Pageable pageable) {
        return ResponseEntity.ok(tarefaService.listarPorAnimal(id, pageable));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar um animal")
    public ResponseEntity<AnimalResponseDTO> atualizar(@PathVariable Long id,
            @Valid @RequestBody AnimalRequestDTO request) {
        return ResponseEntity.ok(service.atualizar(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir um animal")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}