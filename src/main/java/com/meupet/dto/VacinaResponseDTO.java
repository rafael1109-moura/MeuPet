package com.meupet.dto;

import lombok.Data;

@Data
public class VacinaResponseDTO {

    private Long id;
    private String nome;
    private String descricao;
    private Long doencaId;
    private String doencaNome;
    private Integer periodicidadeMeses;
}
