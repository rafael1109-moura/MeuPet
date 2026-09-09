package com.meupet.service;

import java.util.NoSuchElementException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.meupet.dto.AnimalRequestDTO;
import com.meupet.dto.AnimalRequestDTO.TipoAnimal;
import com.meupet.dto.AnimalResponseDTO;
import com.meupet.model.Animal;
import com.meupet.model.Cachorro;
import com.meupet.model.Gato;
import com.meupet.repository.AnimalRepository;

@Service
public class AnimalService {

    private final AnimalRepository repository;

    public AnimalService(AnimalRepository repository) {
        this.repository = repository;
    }

    public AnimalResponseDTO criar(AnimalRequestDTO request) {
        Animal animal = toEntity(request);
        Animal salvo = repository.save(animal);
        return toResponse(salvo);
    }

    public Page<AnimalResponseDTO> listarTodos(Pageable pageable) {
        return repository.findAll(pageable).map(this::toResponse);
    }

    public AnimalResponseDTO buscarPorId(Long id) {
        return toResponse(buscarAnimal(id));
    }

    public AnimalResponseDTO atualizar(Long id, AnimalRequestDTO request) {
        Animal animal = buscarAnimal(id);
        preencherDadosComuns(animal, request);

        if (animal instanceof Cachorro cachorro) {
            validarTipo(request.getTipo(), TipoAnimal.CACHORRO);
            cachorro.setRaca(request.getRacaCachorro());
            cachorro.setDataLastBanho(request.getDataLastBanho());
            cachorro.setDataLastTosa(request.getDataLastTosa());
            cachorro.setDataUltimoPasseio(request.getDataUltimoPasseio());
        }

        if (animal instanceof Gato gato) {
            validarTipo(request.getTipo(), TipoAnimal.GATO);
            gato.setRaca(request.getRacaGato());
            gato.setAreiaSuja(request.isAreiaSuja());
        }

        return toResponse(repository.save(animal));
    }

    public void deletar(Long id) {
        repository.delete(buscarAnimal(id));
    }

    private Animal buscarAnimal(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Animal com id " + id + " não encontrado."));
    }

    private void validarTipo(TipoAnimal informado, TipoAnimal cadastrado) {
        if (informado != null && informado != cadastrado) {
            throw new IllegalArgumentException("Tipo do animal informado não corresponde ao cadastro.");
        }
    }

    private Animal toEntity(AnimalRequestDTO request) {
        if (request.getTipo() == TipoAnimal.CACHORRO) {
            return criarCachorro(request);
        }

        if (request.getTipo() == TipoAnimal.GATO) {
            return criarGato(request);
        }

        throw new IllegalArgumentException("Tipo de animal invalido.");
    }

    private Cachorro criarCachorro(AnimalRequestDTO request) {
        if (request.getRacaCachorro() == null) {
            throw new IllegalArgumentException("Raca do cachorro e obrigatoria.");
        }

        Cachorro cachorro = new Cachorro();
        preencherDadosComuns(cachorro, request);
        cachorro.setRaca(request.getRacaCachorro());
        cachorro.setDataLastBanho(request.getDataLastBanho());
        cachorro.setDataLastTosa(request.getDataLastTosa());
        cachorro.setDataUltimoPasseio(request.getDataUltimoPasseio());
        return cachorro;
    }

    private Gato criarGato(AnimalRequestDTO request) {
        if (request.getRacaGato() == null) {
            throw new IllegalArgumentException("Raca do gato e obrigatoria.");
        }

        Gato gato = new Gato();
        preencherDadosComuns(gato, request);
        gato.setRaca(request.getRacaGato());
        gato.setAreiaSuja(request.isAreiaSuja());
        return gato;
    }

    private void preencherDadosComuns(Animal animal, AnimalRequestDTO request) {
        animal.setNome(request.getNome());
        animal.setIdade(request.getIdade());
        animal.setSexo(request.getSexo());
        animal.setPeso(request.getPeso());
        animal.setSujo(request.isSujo());
        animal.setCastrado(request.isCastrado());
    }

    private AnimalResponseDTO toResponse(Animal animal) {
        AnimalResponseDTO response = new AnimalResponseDTO();
        response.setId(animal.getId());
        response.setNome(animal.getNome());
        response.setIdade(animal.getIdade());
        response.setSexo(animal.getSexo());
        response.setPeso(animal.getPeso());
        response.setSujo(animal.isSujo());
        response.setCastrado(animal.isCastrado());
        response.setSugestaoBrincadeira(animal.sugestoesBrincadeiras());

        if (animal instanceof Cachorro cachorro) {
            preencherDadosCachorro(response, cachorro);
        }

        if (animal instanceof Gato gato) {
            preencherDadosGato(response, gato);
        }

        return response;
    }

    private void preencherDadosCachorro(AnimalResponseDTO response, Cachorro cachorro) {
        response.setTipo(TipoAnimal.CACHORRO);
        response.setRacaCachorro(cachorro.getRaca());
        response.setDataLastBanho(cachorro.getDataLastBanho());
        response.setDataLastTosa(cachorro.getDataLastTosa());
        response.setDataUltimoPasseio(cachorro.getDataUltimoPasseio());
    }

    private void preencherDadosGato(AnimalResponseDTO response, Gato gato) {
        response.setTipo(TipoAnimal.GATO);
        response.setRacaGato(gato.getRaca());
        response.setAreiaSuja(gato.isAreiaSuja());
    }
}