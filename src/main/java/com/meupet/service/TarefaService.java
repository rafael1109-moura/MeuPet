package com.meupet.service;

import com.meupet.dto.TarefaRequestDTO;
import com.meupet.dto.TarefaResponseDTO;
import com.meupet.dto.TarefaResumoDTO;
import com.meupet.model.Animal;
import com.meupet.model.DadoInvalidoException;
import com.meupet.model.Tarefa;
import com.meupet.model.Tarefa.Categoria;
import com.meupet.model.Tarefa.Prioridade;
import com.meupet.model.Vacina;
import com.meupet.repository.AnimalRepository;
import com.meupet.repository.TarefaRepository;
import com.meupet.repository.VacinaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.NoSuchElementException;

@Service
public class TarefaService {

    private final TarefaRepository repository;
    private final AnimalRepository animalRepository;
    private final VacinaRepository vacinaRepository;

    public TarefaService(TarefaRepository repository, AnimalRepository animalRepository,
                         VacinaRepository vacinaRepository) {
        this.repository = repository;
        this.animalRepository = animalRepository;
        this.vacinaRepository = vacinaRepository;
    }

    public TarefaResponseDTO criar(TarefaRequestDTO request) {
        Tarefa tarefa = new Tarefa();
        preencher(tarefa, request);
        tarefa.setConcluida(false);

        return converterParaDTO(repository.save(tarefa));
    }

    public Page<TarefaResponseDTO> listar(Long animalId, Categoria categoria, Boolean concluida,
                                          Boolean apenasAtrasadas, Pageable pageable) {
        return repository.buscarFiltradas(animalId, categoria, concluida, apenasAtrasadas,
                        LocalDate.now(), pageable)
                .map(this::converterParaDTO);
    }

    public Page<TarefaResponseDTO> listarPorAnimal(Long animalId, Pageable pageable) {
        return listar(animalId, null, null, null, pageable);
    }

    public TarefaResponseDTO buscarPorId(Long id) {
        return converterParaDTO(buscarTarefa(id));
    }

    public TarefaResponseDTO atualizar(Long id, TarefaRequestDTO request) {
        Tarefa tarefa = buscarTarefa(id);
        preencher(tarefa, request);

        return converterParaDTO(repository.save(tarefa));
    }

    public TarefaResponseDTO concluir(Long id) {
        Tarefa tarefa = buscarTarefa(id);
        tarefa.setConcluida(true);
        tarefa.setDataConclusao(LocalDate.now());

        return converterParaDTO(repository.save(tarefa));
    }

    public TarefaResponseDTO reabrir(Long id) {
        Tarefa tarefa = buscarTarefa(id);
        tarefa.setConcluida(false);
        tarefa.setDataConclusao(null);

        return converterParaDTO(repository.save(tarefa));
    }

    public void deletar(Long id) {
        repository.delete(buscarTarefa(id));
    }

    public TarefaResumoDTO resumo(Long animalId) {
        LocalDate hoje = LocalDate.now();
        if (animalId != null) {
            animalRepository.findById(animalId)
                    .orElseThrow(() -> new NoSuchElementException("Animal com id " + animalId + " não encontrado."));
        }

        long total = repository.buscarFiltradas(animalId, null, null, null, hoje, Pageable.unpaged()).getTotalElements();
        long pendentes = repository.countByConcluidaFalse();
        long atrasadas = repository.countByConcluidaFalseAndDataPrevistaBefore(hoje);
        long concluidas = repository.countByConcluidaTrue();
        long proximas = repository.countByConcluidaFalseAndDataPrevistaGreaterThanEqual(hoje);

        return new TarefaResumoDTO(total, pendentes, atrasadas, concluidas, proximas);
    }

    private Tarefa buscarTarefa(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Tarefa com id " + id + " não encontrada."));
    }

    private void preencher(Tarefa tarefa, TarefaRequestDTO request) {
        tarefa.setTitulo(request.getTitulo());
        tarefa.setDescricao(request.getDescricao());
        tarefa.setCategoria(request.getCategoria());
        tarefa.setPrioridade(request.getPrioridade() != null ? request.getPrioridade() : Prioridade.MEDIA);
        tarefa.setDataPrevista(request.getDataPrevista());
        tarefa.setAnimal(resolverAnimal(request.getAnimalId()));
        tarefa.setVacina(resolverVacina(request));
    }

    private Animal resolverAnimal(Long animalId) {
        if (animalId == null) {
            return null;
        }
        return animalRepository.findById(animalId)
                .orElseThrow(() -> new NoSuchElementException("Animal com id " + animalId + " não encontrado."));
    }

    private Vacina resolverVacina(TarefaRequestDTO request) {
        String erro = "Vacina com id " + request.getVacinaId() + " não encontrada.";
        if (request.getVacinaId() == null) {
            return null;
        }
        if (request.getCategoria() != Categoria.VACINACAO) {
            throw new NoSuchElementException(erro);
        }
        return vacinaRepository.findById(request.getVacinaId())
                .orElseThrow(() -> new NoSuchElementException(erro));
    }

    private TarefaResponseDTO converterParaDTO(Tarefa tarefa) {
        TarefaResponseDTO response = new TarefaResponseDTO();
        response.setId(tarefa.getId());
        response.setTitulo(tarefa.getTitulo());
        response.setDescricao(tarefa.getDescricao());
        response.setCategoria(tarefa.getCategoria());
        response.setPrioridade(tarefa.getPrioridade());
        response.setDataPrevista(tarefa.getDataPrevista());
        response.setConcluida(tarefa.isConcluida());
        response.setDataConclusao(tarefa.getDataConclusao());

        Animal animal = tarefa.getAnimal();
        if (animal != null) {
            response.setAnimalId(animal.getId());
            response.setAnimalNome(animal.getNome());
        }

        Vacina vacina = tarefa.getVacina();
        if (vacina != null) {
            response.setVacinaId(vacina.getId());
            response.setVacinaNome(vacina.getNome());
            response.setProximaDoseEmMeses(vacina.getPeriodicidadeMeses());
        }

        return response;
    }
}