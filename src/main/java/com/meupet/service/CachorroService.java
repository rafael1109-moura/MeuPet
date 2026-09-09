package com.meupet.service;

import java.util.NoSuchElementException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
        preencher(cachorro, request);

        return toResponse(repository.save(cachorro));
    }

    public Page<CachorroResponseDTO> listarTodos(Pageable pageable) {
        return repository.findAll(pageable).map(this::toResponse);
    }

    public CachorroResponseDTO buscarPorId(Long id) {
        return toResponse(buscarCachorro(id));
    }

    public CachorroResponseDTO atualizar(Long id, CachorroRequestDTO request) throws DadoInvalidoException {
        Cachorro cachorro = buscarCachorro(id);
        preencher(cachorro, request);

        return toResponse(repository.save(cachorro));
    }

    public void deletar(Long id) {
        repository.delete(buscarCachorro(id));
    }

    private Cachorro buscarCachorro(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Cachorro com id " + id + " não encontrado."));
    }

    private void preencher(Cachorro cachorro, CachorroRequestDTO request) {
        cachorro.setNome(request.getNome());
        cachorro.setIdade(request.getIdade());
        cachorro.setRaca(request.getRaca());
        cachorro.setDataLastBanho(request.getDataLastBanho());
        cachorro.setDataLastTosa(request.getDataLastTosa());
        cachorro.setDataUltimoPasseio(request.getDataUltimoPasseio());
        cachorro.setSujo(request.isSujo());
        cachorro.setCastrado(request.isCastrado());
        if (request.getSexo() != null) {
            cachorro.setSexo(request.getSexo());
        }
        if (request.getPeso() != null) {
            cachorro.setPeso(request.getPeso());
        }
    }

    private CachorroResponseDTO toResponse(Cachorro cachorro) {
        CachorroResponseDTO response = new CachorroResponseDTO();
        response.setId(cachorro.getId());
        response.setNome(cachorro.getNome());
        response.setIdade(cachorro.getIdade());
        response.setSexo(cachorro.getSexo());
        response.setPeso(cachorro.getPeso());
        response.setSujo(cachorro.isSujo());
        response.setCastrado(cachorro.isCastrado());
        response.setRaca(cachorro.getRaca());
        response.setDataLastBanho(cachorro.getDataLastBanho());
        response.setDataLastTosa(cachorro.getDataLastTosa());
        response.setDataUltimoPasseio(cachorro.getDataUltimoPasseio());
        return response;
    }
}