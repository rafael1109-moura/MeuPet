package com.meupet.service;

import com.meupet.dto.DoencaRequestDTO;
import com.meupet.dto.DoencaResponseDTO;
import com.meupet.model.DadoInvalidoException;
import com.meupet.model.Doenca;
import com.meupet.repository.DoencaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class DoencaService {

    private final DoencaRepository repository;

    public DoencaService(DoencaRepository repository) {
        this.repository = repository;
    }

    public DoencaResponseDTO criar(DoencaRequestDTO request) throws DadoInvalidoException {
        validarNomeDisponivel(request.getNome(), null);

        Doenca doenca = new Doenca(null, request.getNome(), request.getDescricao(), request.getTratamento());
        return converterParaDTO(repository.save(doenca));
    }

    public Page<DoencaResponseDTO> listarTodas(Pageable pageable) {
        return repository.findAll(pageable).map(this::converterParaDTO);
    }

    public DoencaResponseDTO buscarPorId(Long id) {
        return converterParaDTO(buscarDoenca(id));
    }

    public DoencaResponseDTO atualizar(Long id, DoencaRequestDTO request) throws DadoInvalidoException {
        Doenca doenca = buscarDoenca(id);
        validarNomeDisponivel(request.getNome(), id);

        doenca.setNome(request.getNome());
        doenca.setDescricao(request.getDescricao());
        doenca.setTratamento(request.getTratamento());

        return converterParaDTO(repository.save(doenca));
    }

    public void deletar(Long id) {
        repository.delete(buscarDoenca(id));
    }

    private Doenca buscarDoenca(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Doença com id " + id + " não encontrada."));
    }

    private void validarNomeDisponivel(String nome, Long idIgnorado) throws DadoInvalidoException {
        Optional<Doenca> existente = repository.findByNomeIgnoreCase(nome);
        if (existente.isPresent() && !existente.get().getId().equals(idIgnorado)) {
            throw new DadoInvalidoException("A doença '" + nome + "' já está cadastrada.");
        }
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