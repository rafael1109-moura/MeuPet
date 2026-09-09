package com.meupet.dto;

import com.meupet.model.Animal.Sexo;
import com.meupet.model.Gato.RacaGato;

import lombok.Data;

@Data
public class GatoResponseDTO {

    private Long id;
    private String nome;
    private int idade;
    private Sexo sexo;
    private Float peso;
    private boolean sujo;
    private boolean castrado;
    private RacaGato raca;
    private boolean areiaSuja;
}