/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.meupet.dto;

/**
 *
 * @author D410W
 */

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UsuarioRequestDTO {

    @NotBlank(message = "O nome do usuario é obrigatório.")
    private String nome;

    @NotBlank(message = "O e-mail do usuário é obrigatório.")
    private String email;

    @NotBlank(message = "A senha do usuário é obrigatória.")
    private String senha;
}
