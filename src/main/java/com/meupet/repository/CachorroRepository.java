package com.meupet.repository;

import com.meupet.model.Cachorro;
import com.meupet.model.Cachorro.RacaCachorro;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CachorroRepository extends JpaRepository<Cachorro, Long> {

    Optional<Cachorro> findByNomeIgnoreCase(String nome);

    List<Cachorro> findByRaca(RacaCachorro raca);

    List<Cachorro> findByDataLastBanhoBefore(LocalDate data);

    List<Cachorro> findByDataLastTosaBefore(LocalDate data);

    List<Cachorro> findByDataUltimoPasseioBefore(LocalDate data);

    Page<Cachorro> findAllByUsuarioId(Long usuarioId, Pageable pageable);

    Optional<Cachorro> findByIdAndUsuarioId(Long id, Long usuarioId);
}