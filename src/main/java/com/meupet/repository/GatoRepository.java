package com.meupet.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.meupet.model.Gato;
import com.meupet.model.Gato.RacaGato;

// Repository para consultas de gatos.
@Repository
public interface GatoRepository extends JpaRepository<Gato, Integer> {

    Optional<Gato> findByNomeIgnoreCase(String nome);

    List<Gato> findByRaca(RacaGato raca);
}
