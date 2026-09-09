package com.meupet.dto;

import com.meupet.model.Gato.RacaGato;

import lombok.Data;

// DTO usado para devolver dados de gatos.
@Data
public class GatoResponseDTO {

    private Long id;
    private String nome;
    private int idade;
    private RacaGato raca;
    private boolean areiaSuja;
}
