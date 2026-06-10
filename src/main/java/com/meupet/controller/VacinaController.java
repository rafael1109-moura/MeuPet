package com.meupet.controller;

import com.meupet.dto.VacinaRequestDTO;
import com.meupet.dto.VacinaResponseDTO;
import com.meupet.model.DadoInvalidoException;
import com.meupet.service.VacinaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
        VacinaResponseDTO novaVacina = service.criar(request);
        return new ResponseEntity<>(novaVacina, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Listar todas as vacinas cadastradas")
    public ResponseEntity<List<VacinaResponseDTO>> listar() {
        List<VacinaResponseDTO> lista = service.listarTodas();
        return ResponseEntity.ok(lista);
    }
}
