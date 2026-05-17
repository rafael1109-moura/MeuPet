/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.meupet.service;

/**
 *
 * @author edvaldinhs
 */
import com.meupet.dto.DoencaRequestDTO;
import com.meupet.dto.DoencaResponseDTO;
import com.meupet.model.DadoInvalidoException;
import com.meupet.model.Doenca;
import com.meupet.repository.DoencaRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DoencaService {

    private final DoencaRepository repository;

    public DoencaService(DoencaRepository repository) {
        this.repository = repository;
    }

    public DoencaResponseDTO criar(DoencaRequestDTO request) throws DadoInvalidoException {
        if (repository.findByNomeIgnoreCase(request.getNome()).isPresent()) {
            throw new DadoInvalidoException("A doença '" + request.getNome() + "' já está cadastrada.");
        }

        Doenca doenca = new Doenca(null, request.getNome(), request.getDescricao(), request.getTratamento());
        Doenca salva = repository.save(doenca);
        
        return converterParaDTO(salva);
    }

    public List<DoencaResponseDTO> listarTodas() {
        return repository.findAll().stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    private DoencaResponseDTO converterParaDTO(Doenca doenca) {
        DoencaResponseDTO response = new DoencaResponseDTO();
        response.setId(doenca.getId());
        response.setNome(doenca.getNome());
        response.setDescricao(doenca.getDescricao());
        response.setTratamento(doenca.getTratamento());
        return response;
    }
}