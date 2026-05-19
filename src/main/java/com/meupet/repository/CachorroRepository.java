package com.meupet.repository;

import java.time.LocalDate;
import java.util.List; // importa o enum de raça
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.meupet.model.Cachorro;
import com.meupet.model.Cachorro.RacaCachorro;


@Repository
public interface CachorroRepository extends JpaRepository<Cachorro, Integer> {
   
    Optional<Cachorro> findByNomeIgnoreCase(String nome);
    
    List<Cachorro> findByRaca(RacaCachorro raca);
    
    List<Cachorro> findByDataLastBanhoBefore(LocalDate data);
    
    List<Cachorro> findByDataLastTosaBefore(LocalDate data);
    
    List<Cachorro> findByDataUltimoPasseioBefore(LocalDate data);
}
