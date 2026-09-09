package com.meupet.repository;

import com.meupet.model.Gato;
import com.meupet.model.Gato.RacaGato;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

// Repository para consultas de gatos.
@Repository
public interface GatoRepository extends JpaRepository<Gato, Long> {

    Optional<Gato> findByNomeIgnoreCase(String nome);

    List<Gato> findByRaca(RacaGato raca);

    Page<Gato> findAllByUsuarioId(Long usuarioId, Pageable pageable);

    Optional<Gato> findByIdAndUsuarioId(Long id, Long usuarioId);
}