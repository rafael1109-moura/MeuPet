package com.meupet.dto;

import com.meupet.model.Tarefa.Categoria;
import com.meupet.model.Tarefa.Prioridade;

import lombok.Data;

import java.time.LocalDate;

@Data
public class TarefaResponseDTO {

    private Long id;
    private String titulo;
    private String descricao;
    private Categoria categoria;
    private Prioridade prioridade;
    private LocalDate dataPrevista;
    private boolean concluida;
    private LocalDate dataConclusao;
    private Long animalId;
    private String animalNome;
    private Long vacinaId;
    private String vacinaNome;
    private Integer proximaDoseEmMeses;
}