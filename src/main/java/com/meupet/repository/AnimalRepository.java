package com.meupet.repository;

import com.meupet.model.Animal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

// Repository generico para a hierarquia de animais.
@Repository
public interface AnimalRepository extends JpaRepository<Animal, Long> {

    Page<Animal> findAllByUsuarioId(Long usuarioId, Pageable pageable);

    Optional<Animal> findByIdAndUsuarioId(Long id, Long usuarioId);
}