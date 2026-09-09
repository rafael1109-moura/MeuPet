package com.meupet.controller;

import com.meupet.dto.VacinaRequestDTO;
import com.meupet.dto.VacinaResponseDTO;
import com.meupet.model.DadoInvalidoException;
import com.meupet.service.VacinaService;
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
@RequestMapping("/api/vacinas")
@Tag(name = "Vacinas", description = "Endpoints para gerenciamento de vacinas")
public class VacinaController {

    private final VacinaService service;

    public VacinaController(VacinaService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Cadastrar uma nova vacina")
    public ResponseEntity<VacinaResponseDTO> cadastrar(@Valid @RequestBody VacinaRequestDTO request)
            throws DadoInvalidoException {
        return new ResponseEntity<>(service.criar(request), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Listar todas as vacinas cadastradas")
    public ResponseEntity<Page<VacinaResponseDTO>> listar(
            @PageableDefault(size = 10, sort = "id") Pageable pageable) {
        return ResponseEntity.ok(service.listarTodas(pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar vacina por id")
    public ResponseEntity<VacinaResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar uma vacina")
    public ResponseEntity<VacinaResponseDTO> atualizar(@PathVariable Long id,
            @Valid @RequestBody VacinaRequestDTO request) throws DadoInvalidoException {
        return ResponseEntity.ok(service.atualizar(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir uma vacina")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}