package com.meupet.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "tb_tarefa")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Tarefa {

    public enum Categoria {
        PESSOAL, VACINACAO, CONSULTA
    }

    public enum Prioridade {
        BAIXA, MEDIA, ALTA
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Categoria categoria;

    @Enumerated(EnumType.STRING)
    private Prioridade prioridade;

    @Column(name = "data_prevista", nullable = false)
    private LocalDate dataPrevista;

    @Column(nullable = false)
    private boolean concluida;

    @Column(name = "data_conclusao")
    private LocalDate dataConclusao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "animal_id")
    private Animal animal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vacina_id")
    private Vacina vacina;
}