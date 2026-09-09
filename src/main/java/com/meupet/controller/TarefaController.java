package com.meupet.controller;

import com.meupet.dto.TarefaRequestDTO;
import com.meupet.dto.TarefaResponseDTO;
import com.meupet.dto.TarefaResumoDTO;
import com.meupet.model.Tarefa.Categoria;
import com.meupet.service.TarefaService;
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
@RequestMapping("/api/tarefas")
@Tag(name = "Tarefas", description = "Endpoints para gerenciamento de tarefas e lembretes")
public class TarefaController {

    private final TarefaService service;

    public TarefaController(TarefaService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Criar uma nova tarefa")
    public ResponseEntity<TarefaResponseDTO> criar(@Valid @RequestBody TarefaRequestDTO request) {
        return new ResponseEntity<>(service.criar(request), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Listar tarefas com filtros")
    public ResponseEntity<Page<TarefaResponseDTO>> listar(
            @RequestParam(required = false) Long animalId,
            @RequestParam(required = false) Categoria categoria,
            @RequestParam(required = false) Boolean concluida,
            @RequestParam(required = false) Boolean atrasadas,
            @PageableDefault(size = 10, sort = "dataPrevista") Pageable pageable) {
        return ResponseEntity.ok(service.listar(animalId, categoria, concluida, atrasadas, pageable));
    }

    @GetMapping("/resumo")
    @Operation(summary = "Resumo das tarefas para painel")
    public ResponseEntity<TarefaResumoDTO> resumo(@RequestParam(required = false) Long animalId) {
        return ResponseEntity.ok(service.resumo(animalId));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar tarefa por id")
    public ResponseEntity<TarefaResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar uma tarefa")
    public ResponseEntity<TarefaResponseDTO> atualizar(@PathVariable Long id,
            @Valid @RequestBody TarefaRequestDTO request) {
        return ResponseEntity.ok(service.atualizar(id, request));
    }

    @PatchMapping("/{id}/concluir")
    @Operation(summary = "Marcar tarefa como concluida")
    public ResponseEntity<TarefaResponseDTO> concluir(@PathVariable Long id) {
        return ResponseEntity.ok(service.concluir(id));
    }

    @PatchMapping("/{id}/reabrir")
    @Operation(summary = "Reabrir uma tarefa concluida")
    public ResponseEntity<TarefaResponseDTO> reabrir(@PathVariable Long id) {
        return ResponseEntity.ok(service.reabrir(id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir uma tarefa")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}