/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.meupet.dto;

/**
 *
 * @author edvaldinhs
 */

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DoencaRequestDTO {

    @NotBlank(message = "O nome da doença é obrigatório.")
    private String nome;

    @NotBlank(message = "A descrição da doença é obrigatória.")
    private String descricao;

    private String tratamento;
}