package com.meupet.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.meupet.dto.CachorroRequestDTO;
import com.meupet.dto.CachorroResponseDTO;
import com.meupet.model.Cachorro;
import com.meupet.model.DadoInvalidoException;
import com.meupet.repository.CachorroRepository;

@Service
public class CachorroService {

    private final CachorroRepository repository;

    public CachorroService(CachorroRepository repository) {
        this.repository = repository;
    }

    public CachorroResponseDTO criar(CachorroRequestDTO request) throws DadoInvalidoException {
        Cachorro cachorro = new Cachorro();
        cachorro.setNome(request.getNome());
        cachorro.setIdade(request.getIdade());
        cachorro.setRaca(request.getRaca());
        cachorro.setDataLastBanho(request.getDataLastBanho());
        cachorro.setDataLastTosa(request.getDataLastTosa());
        cachorro.setDataUltimoPasseio(request.getDataUltimoPasseio());

        return toResponse(repository.save(cachorro));
    }

    public List<CachorroResponseDTO> listarTodos() {
        return repository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private CachorroResponseDTO toResponse(Cachorro cachorro) {
        CachorroResponseDTO response = new CachorroResponseDTO();
        response.setId(cachorro.getId());
        response.setNome(cachorro.getNome());
        response.setIdade(cachorro.getIdade());
        response.setRaca(cachorro.getRaca());
        response.setDataLastBanho(cachorro.getDataLastBanho());
        response.setDataLastTosa(cachorro.getDataLastTosa());
        response.setDataUltimoPasseio(cachorro.getDataUltimoPasseio());
        return response;
    }
}
