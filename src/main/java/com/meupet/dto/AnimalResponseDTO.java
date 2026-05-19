package com.meupet.dto;

import java.time.LocalDate;

import com.meupet.dto.AnimalRequestDTO.TipoAnimal;
import com.meupet.model.Animal.Sexo;
import com.meupet.model.Cachorro.RacaCachorro;
import com.meupet.model.Gato.RacaGato;

import lombok.Data;

// DTO usado para devolver dados de animais pela API.
@Data
public class AnimalResponseDTO {

    // Dados comuns de Animal.
    private Integer id;
    private TipoAnimal tipo;
    private String nome;
    private int idade;
    private Sexo sexo;
    private float peso;
    private boolean sujo;
    private boolean castrado;
    private String sugestaoBrincadeira;

    // Dados especificos de Cachorro.
    private RacaCachorro racaCachorro;
    private LocalDate dataLastBanho;
    private LocalDate dataLastTosa;
    private LocalDate dataUltimoPasseio;

    // Dados especificos de Gato.
    private RacaGato racaGato;
    private boolean areiaSuja;
}
