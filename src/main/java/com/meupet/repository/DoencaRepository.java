/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.meupet.repository;

/**
 *
 * @author edvaldinhs
 */

import com.meupet.model.Doenca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface DoencaRepository extends JpaRepository<Doenca, Long> {
    
    Optional<Doenca> findByNomeIgnoreCase(String nome);
}
