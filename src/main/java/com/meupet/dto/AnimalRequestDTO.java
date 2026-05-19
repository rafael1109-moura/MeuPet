package com.meupet.dto;

import java.time.LocalDate;

import com.meupet.model.Animal.Sexo;
import com.meupet.model.Cachorro.RacaCachorro;
import com.meupet.model.Gato.RacaGato;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

// DTO usado para receber dados de criacao de animais.
@Data
public class AnimalRequestDTO {

    // Tipo concreto que sera criado.
    @NotNull(message = "O tipo do animal e obrigatorio")
    private TipoAnimal tipo;

    // Dados comuns de Animal.
    @NotBlank(message = "O nome do animal e obrigatorio")
    private String nome;

    @PositiveOrZero(message = "A idade deve ser positiva")
    private int idade;

    private Sexo sexo;

    @PositiveOrZero(message = "O peso deve ser positivo")
    private float peso;

    private boolean sujo;
    private boolean castrado;

    // Dados especificos de Cachorro.
    private RacaCachorro racaCachorro;
    private LocalDate dataLastBanho;
    private LocalDate dataLastTosa;
    private LocalDate dataUltimoPasseio;

    // Dados especificos de Gato.
    private RacaGato racaGato;
    private boolean areiaSuja;

    // Tipos de animais aceitos no endpoint generico.
    public enum TipoAnimal {
        CACHORRO, GATO
    }
}
