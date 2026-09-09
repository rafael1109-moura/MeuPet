package com.meupet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.meupet.model.Animal;

// Repository generico para a hierarquia de animais.
@Repository
public interface AnimalRepository extends JpaRepository<Animal, Long> {
}
