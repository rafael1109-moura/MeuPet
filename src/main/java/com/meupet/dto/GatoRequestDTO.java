package com.meupet.dto;

import com.meupet.model.Animal.Sexo;
import com.meupet.model.Gato.RacaGato;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class GatoRequestDTO {

    @NotBlank(message = "O nome do gato e obrigatorio")
    private String nome;

    @NotNull(message = "A idade e obrigatoria")
    private Integer idade;

    @NotNull(message = "A raca e obrigatoria")
    private RacaGato raca;

    private Sexo sexo;

    @PositiveOrZero(message = "O peso deve ser positivo")
    private Float peso;

    private boolean sujo;
    private boolean castrado;

    private boolean areiaSuja;
}