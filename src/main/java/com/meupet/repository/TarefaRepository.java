package com.meupet.repository;

import com.meupet.model.Tarefa;
import com.meupet.model.Tarefa.Categoria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface TarefaRepository extends JpaRepository<Tarefa, Long> {

    @Query("""
           select t from Tarefa t
           where (:animalId is null or t.animal.id = :animalId)
             and (:categoria is null or t.categoria = :categoria)
             and (:concluida is null or t.concluida = :concluida)
             and (:apenasAtrasadas is null or (t.dataPrevista < :hoje and t.concluida = false))
           """)
    Page<Tarefa> buscarFiltradas(@Param("animalId") Long animalId,
                                 @Param("categoria") Categoria categoria,
                                 @Param("concluida") Boolean concluida,
                                 @Param("apenasAtrasadas") Boolean apenasAtrasadas,
                                 @Param("hoje") LocalDate hoje,
                                 Pageable pageable);

    long countByConcluidaFalseAndDataPrevistaBefore(LocalDate data);

    long countByConcluidaFalse();

    long countByConcluidaTrue();

    long countByConcluidaFalseAndDataPrevistaGreaterThanEqual(LocalDate data);
}