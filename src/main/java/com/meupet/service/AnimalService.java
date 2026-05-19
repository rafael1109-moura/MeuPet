package com.meupet.service;

import org.springframework.stereotype.Service;

import com.meupet.dto.AnimalRequestDTO;
import com.meupet.dto.AnimalRequestDTO.TipoAnimal;
import com.meupet.dto.AnimalResponseDTO;
import com.meupet.model.Animal;
import com.meupet.model.Cachorro;
import com.meupet.model.Gato;
import com.meupet.repository.AnimalRepository;

// Service com as regras de negocio de Animal.
@Service
public class AnimalService {

    private final AnimalRepository repository;

    public AnimalService(AnimalRepository repository) {
        this.repository = repository;
    }

    // Cria um animal concreto a partir do DTO.
    public AnimalResponseDTO criar(AnimalRequestDTO request) {
        Animal animal = toEntity(request);
        Animal salvo = repository.save(animal);
        return toResponse(salvo);
    }

    // Decide qual subclasse deve ser criada.
    private Animal toEntity(AnimalRequestDTO request) {
        if (request.getTipo() == TipoAnimal.CACHORRO) {
            return criarCachorro(request);
        }

        if (request.getTipo() == TipoAnimal.GATO) {
            return criarGato(request);
        }

        throw new IllegalArgumentException("Tipo de animal invalido.");
    }

    // Monta a entidade Cachorro.
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

    // Monta a entidade Gato.
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

    // Preenche os campos herdados de Animal.
    private void preencherDadosComuns(Animal animal, AnimalRequestDTO request) {
        animal.setNome(request.getNome());
        animal.setIdade(request.getIdade());
        animal.setSexo(request.getSexo());
        animal.setPeso(request.getPeso());
        animal.setSujo(request.isSujo());
        animal.setCastrado(request.isCastrado());
    }

    // Converte entidade em DTO de resposta.
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

    // Preenche dados especificos de cachorro no DTO.
    private void preencherDadosCachorro(AnimalResponseDTO response, Cachorro cachorro) {
        response.setTipo(TipoAnimal.CACHORRO);
        response.setRacaCachorro(cachorro.getRaca());
        response.setDataLastBanho(cachorro.getDataLastBanho());
        response.setDataLastTosa(cachorro.getDataLastTosa());
        response.setDataUltimoPasseio(cachorro.getDataUltimoPasseio());
    }

    // Preenche dados especificos de gato no DTO.
    private void preencherDadosGato(AnimalResponseDTO response, Gato gato) {
        response.setTipo(TipoAnimal.GATO);
        response.setRacaGato(gato.getRaca());
        response.setAreiaSuja(gato.isAreiaSuja());
    }
}
