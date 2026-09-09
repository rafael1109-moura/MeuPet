package com.meupet.dto;

import java.time.LocalDate;

import com.meupet.model.Cachorro.RacaCachorro;

import lombok.Data;

@Data
public class CachorroResponseDTO {

    private Long id; // ID do banco

    private String nome;
    private int idade;

    private RacaCachorro raca;

    private LocalDate dataLastBanho;
    private LocalDate dataLastTosa;
    private LocalDate dataUltimoPasseio;
}
