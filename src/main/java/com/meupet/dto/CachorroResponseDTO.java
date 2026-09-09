package com.meupet.dto;

import java.time.LocalDate;

import com.meupet.model.Animal.Sexo;
import com.meupet.model.Cachorro.RacaCachorro;

import lombok.Data;

@Data
public class CachorroResponseDTO {

    private Long id;

    private String nome;
    private int idade;
    private Sexo sexo;
    private Float peso;
    private boolean sujo;
    private boolean castrado;

    private RacaCachorro raca;

    private LocalDate dataLastBanho;
    private LocalDate dataLastTosa;
    private LocalDate dataUltimoPasseio;
}