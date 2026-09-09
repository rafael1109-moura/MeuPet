package com.meupet.service;

import com.meupet.dto.VacinaRequestDTO;
import com.meupet.dto.VacinaResponseDTO;
import com.meupet.model.DadoInvalidoException;
import com.meupet.model.Doenca;
import com.meupet.model.Vacina;
import com.meupet.repository.DoencaRepository;
import com.meupet.repository.VacinaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class VacinaService {

    private final VacinaRepository repository;
    private final DoencaRepository doencaRepository;

    public VacinaService(VacinaRepository repository, DoencaRepository doencaRepository) {
        this.repository = repository;
        this.doencaRepository = doencaRepository;
    }

    public VacinaResponseDTO criar(VacinaRequestDTO request) throws DadoInvalidoException {
        validarNomeDisponivel(request.getNome(), null);

        Doenca doenca = buscarDoenca(request.getDoencaId());
        Vacina vacina = new Vacina(null, request.getNome(), request.getDescricao(), doenca);

        return converterParaDTO(repository.save(vacina));
    }

    public Page<VacinaResponseDTO> listarTodas(Pageable pageable) {
        return repository.findAll(pageable).map(this::converterParaDTO);
    }

    public VacinaResponseDTO buscarPorId(Long id) {
        return converterParaDTO(buscarVacina(id));
    }

    public VacinaResponseDTO atualizar(Long id, VacinaRequestDTO request) throws DadoInvalidoException {
        Vacina vacina = buscarVacina(id);
        validarNomeDisponivel(request.getNome(), id);

        vacina.setNome(request.getNome());
        vacina.setDescricao(request.getDescricao());
        vacina.setDoenca(buscarDoenca(request.getDoencaId()));

        return converterParaDTO(repository.save(vacina));
    }

    public void deletar(Long id) {
        repository.delete(buscarVacina(id));
    }

    private Vacina buscarVacina(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Vacina com id " + id + " não encontrada."));
    }

    private void validarNomeDisponivel(String nome, Long idIgnorado) throws DadoInvalidoException {
        Optional<Vacina> existente = repository.findByNomeIgnoreCase(nome);
        if (existente.isPresent() && !existente.get().getId().equals(idIgnorado)) {
            throw new DadoInvalidoException("A vacina '" + nome + "' já está cadastrada.");
        }
    }

    private Doenca buscarDoenca(Long doencaId) throws DadoInvalidoException {
        if (doencaId == null) {
            return null;
        }

        return doencaRepository.findById(doencaId)
                .orElseThrow(() -> new DadoInvalidoException("Doença com id " + doencaId + " não encontrada."));
    }

    private VacinaResponseDTO converterParaDTO(Vacina vacina) {
        VacinaResponseDTO response = new VacinaResponseDTO();
        response.setId(vacina.getId());
        response.setNome(vacina.getNome());
        response.setDescricao(vacina.getDescricao());

        Doenca doenca = vacina.getDoenca();
        if (doenca != null) {
            response.setDoencaId(doenca.getId());
            response.setDoencaNome(doenca.getNome());
        }

        return response;
    }
}