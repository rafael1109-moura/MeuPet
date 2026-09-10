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
import java.util.Optional;

@Repository
public interface TarefaRepository extends JpaRepository<Tarefa, Long> {

    @Query("""
           select t from Tarefa t
           where t.usuario.id = :usuarioId
             and (:animalId is null or t.animal.id = :animalId)
             and (:categoria is null or t.categoria = :categoria)
             and (:concluida is null or t.concluida = :concluida)
             and (:apenasAtrasadas is null or (t.dataPrevista < :hoje and t.concluida = false))
           """)
    Page<Tarefa> buscarFiltradas(@Param("usuarioId") Long usuarioId,
                                 @Param("animalId") Long animalId,
                                 @Param("categoria") Categoria categoria,
                                 @Param("concluida") Boolean concluida,
                                 @Param("apenasAtrasadas") Boolean apenasAtrasadas,
                                 @Param("hoje") LocalDate hoje,
                                 Pageable pageable);

    Optional<Tarefa> findByIdAndUsuarioId(Long id, Long usuarioId);

    long countByConcluidaFalseAndAnimalIdAndUsuarioId(Long animalId, Long usuarioId);

    long countByConcluidaFalseAndDataPrevistaBeforeAndAnimalIdAndUsuarioId(LocalDate data, Long animalId, Long usuarioId);

    long countByConcluidaTrueAndAnimalIdAndUsuarioId(Long animalId, Long usuarioId);

    long countByConcluidaFalseAndDataPrevistaGreaterThanEqualAndAnimalIdAndUsuarioId(LocalDate data, Long animalId, Long usuarioId);

    long countByConcluidaFalseAndUsuarioId(Long usuarioId);

    long countByConcluidaFalseAndDataPrevistaBeforeAndUsuarioId(LocalDate data, Long usuarioId);

    long countByConcluidaTrueAndUsuarioId(Long usuarioId);

    long countByConcluidaFalseAndDataPrevistaGreaterThanEqualAndUsuarioId(LocalDate data, Long usuarioId);
}