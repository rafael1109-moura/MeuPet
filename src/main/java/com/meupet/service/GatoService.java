package com.meupet.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.meupet.dto.GatoRequestDTO;
import com.meupet.dto.GatoResponseDTO;
import com.meupet.model.Gato;
import com.meupet.repository.GatoRepository;

// Service com as regras de negocio de gatos.
@Service
public class GatoService {

    private final GatoRepository repository;

    public GatoService(GatoRepository repository) {
        this.repository = repository;
    }

    // Cria um gato a partir do DTO.
    public GatoResponseDTO criar(GatoRequestDTO request) {
        Gato gato = new Gato();
        gato.setNome(request.getNome());
        gato.setIdade(request.getIdade());
        gato.setRaca(request.getRaca());
        gato.setAreiaSuja(request.isAreiaSuja());

        return toResponse(repository.save(gato));
    }

    // Lista todos os gatos cadastrados.
    public List<GatoResponseDTO> listarTodos() {
        return repository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // Converte entidade para DTO de resposta.
    private GatoResponseDTO toResponse(Gato gato) {
        GatoResponseDTO response = new GatoResponseDTO();
        response.setId(gato.getId());
        response.setNome(gato.getNome());
        response.setIdade(gato.getIdade());
        response.setRaca(gato.getRaca());
        response.setAreiaSuja(gato.isAreiaSuja());
        return response;
    }
}
