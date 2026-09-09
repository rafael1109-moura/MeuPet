package com.meupet.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UsuarioRequestDTO {

    @NotBlank(message = "O nome do usuario e obrigatorio.")
    @Size(min = 2, max = 100, message = "O nome deve ter entre 2 e 100 caracteres.")
    private String nome;

    @NotBlank(message = "O e-mail do usuario e obrigatorio.")
    @Email(message = "Formato de e-mail invalido.")
    @Size(max = 255, message = "O e-mail deve ter no maximo 255 caracteres.")
    private String email;

    @NotBlank(message = "A senha do usuario e obrigatoria.")
    @Size(min = 8, max = 128, message = "A senha deve ter entre 8 e 128 caracteres.")
    private String senha;
}
