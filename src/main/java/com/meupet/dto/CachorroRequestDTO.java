package com.meupet.dto;

import java.time.LocalDate;

import com.meupet.model.Animal.Sexo;
import com.meupet.model.Cachorro.RacaCachorro;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class CachorroRequestDTO {

    @NotBlank(message = "O nome do cachorro é obrigatório")
    private String nome;

    @NotNull(message = "A idade é obrigatória")
    private Integer idade;

    @NotNull(message = "A raça é obrigatória")
    private RacaCachorro raca;

    private Sexo sexo;

    @PositiveOrZero(message = "O peso deve ser positivo")
    private Float peso;

    private boolean sujo;
    private boolean castrado;

    private LocalDate dataLastBanho;
    private LocalDate dataLastTosa;
    private LocalDate dataUltimoPasseio;
}