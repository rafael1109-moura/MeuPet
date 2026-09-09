package com.meupet.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TarefaResumoDTO {
    private long total;
    private long pendentes;
    private long atrasadas;
    private long concluidas;
    private long proximas;
}