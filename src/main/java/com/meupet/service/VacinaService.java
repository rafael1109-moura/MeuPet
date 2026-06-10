package com.meupet.service;

import com.meupet.dto.VacinaRequestDTO;
import com.meupet.dto.VacinaResponseDTO;
import com.meupet.model.DadoInvalidoException;
import com.meupet.model.Doenca;
import com.meupet.model.Vacina;
import com.meupet.repository.DoencaRepository;
import com.meupet.repository.VacinaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VacinaService {

    private final VacinaRepository repository;
    private final DoencaRepository doencaRepository;

    public VacinaService(VacinaRepository repository, DoencaRepository doencaRepository) {
        this.repository = repository;
        this.doencaRepository = doencaRepository;
    }

    public VacinaResponseDTO criar(VacinaRequestDTO request) throws DadoInvalidoException {
        if (repository.findByNomeIgnoreCase(request.getNome()).isPresent()) {
            throw new DadoInvalidoException("A vacina '" + request.getNome() + "' já está cadastrada.");
        }

        Doenca doenca = buscarDoenca(request.getDoencaId());
        Vacina vacina = new Vacina(null, request.getNome(), request.getDescricao(), doenca);

        return converterParaDTO(repository.save(vacina));
    }

    public List<VacinaResponseDTO> listarTodas() {
        return repository.findAll()
                .stream()
                .map(this::converterParaDTO)
                .toList();
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
