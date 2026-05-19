package com.meupet.dto;

import java.time.LocalDate;

import com.meupet.model.Cachorro.RacaCachorro;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data // gera getters, setters, toString..
public class CachorroRequestDTO {

    @NotBlank(message = "O nome do cachorro é obrigatório")
    private String nome; // vem do Animal

    @NotNull(message = "A idade é obrigatória")
    private Integer idade; // Integer pra validação funcionar

    @NotNull(message = "A raça é obrigatória")
    private RacaCachorro raca;

    private LocalDate dataLastBanho; // opcional
    private LocalDate dataLastTosa; // opcional
    private LocalDate dataUltimoPasseio; // opcional

    public CachorroRequestDTO() {
    }
}