package com.meupet.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tb_doenca")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Doenca {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nome;

    private String descricao;
    private String tratamento;
}