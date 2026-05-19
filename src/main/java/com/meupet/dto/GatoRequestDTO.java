package com.meupet.dto;

import com.meupet.model.Gato.RacaGato;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

// DTO usado para cadastrar gatos.
@Data
public class GatoRequestDTO {

    @NotBlank(message = "O nome do gato e obrigatorio")
    private String nome;

    @NotNull(message = "A idade e obrigatoria")
    private Integer idade;

    @NotNull(message = "A raca e obrigatoria")
    private RacaGato raca;

    private boolean areiaSuja;
}
