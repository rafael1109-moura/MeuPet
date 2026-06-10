package com.meupet.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class VacinaRequestDTO {

    @NotBlank(message = "O nome da vacina é obrigatório.")
    private String nome;

    @NotBlank(message = "A descrição da vacina é obrigatória.")
    private String descricao;

    private Long doencaId;
}
