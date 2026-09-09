package com.meupet.dto;

import com.meupet.model.Tarefa.Categoria;
import com.meupet.model.Tarefa.Prioridade;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class TarefaRequestDTO {

    @NotBlank(message = "O titulo da tarefa e obrigatorio.")
    private String titulo;

    private String descricao;

    @NotNull(message = "A categoria da tarefa e obrigatoria.")
    private Categoria categoria;

    private Prioridade prioridade;

    @NotNull(message = "A data prevista e obrigatoria.")
    private LocalDate dataPrevista;

    private Long animalId;

    private Long vacinaId;
}