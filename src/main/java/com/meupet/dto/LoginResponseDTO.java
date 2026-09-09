package com.meupet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponseDTO {
    private String token;
    private Long usuarioId;
    private String nome;
    private String email;
}