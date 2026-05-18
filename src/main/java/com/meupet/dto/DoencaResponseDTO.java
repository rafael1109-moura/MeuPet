/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.meupet.dto;

/**
 *
 * @author edvaldinhs
 */
import lombok.Data;

@Data
public class DoencaResponseDTO {
    private Long id;
    private String nome;
    private String descricao;
    private String tratamento;
}