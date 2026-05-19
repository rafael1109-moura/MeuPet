package com.meupet.model;

import java.util.List;
import java.util.Map;
import java.util.Random;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.Setter;

// Entidade que representa um gato.
@Entity
@Table(name = "tb_gato")
@Getter
@Setter
public class Gato extends Animal {

    // Informa se a caixa de areia precisa ser limpa.
    private boolean areiaSuja;

    // Raca do gato, salva como enum.
    @Enumerated(EnumType.STRING)
    private RacaGato raca;

    // Opcoes de raca aceitas para gatos.
    public enum RacaGato {
        Maine_Coon, Persa, Siames, SRD
    }

    // Construtor vazio exigido pelo JPA.
    public Gato() {
        super();
    }

    // Construtor com dados comuns e dados especificos de gato.
    public Gato(Integer id, String nome, int idade, Sexo sexo, float peso, boolean sujo, boolean castrado,
                boolean areiaSuja, RacaGato raca) {
        super(id, nome, idade, sexo, peso, sujo, castrado);
        this.areiaSuja = areiaSuja;
        this.raca = raca;
    }

    // Retorna as vacinas recomendadas para gatos.
    @Override
    public List<Vacina> buscarVacinas(Map<String, List<Vacina>> vacinasPorPet) {
        return vacinasPorPet.get("Gato");
    }

    // Retorna as doencas cadastradas para gatos.
    @Override
    public List<Doenca> buscarDoencas(Map<String, List<Doenca>> doencasPorPet) {
        return doencasPorPet.get("Gato");
    }

    // Marca a caixa de areia como limpa.
    public void limparAreia() {
        this.areiaSuja = false;
    }

    // Metodo auxiliar antigo para compatibilidade com o codigo de console.
    @Transient
    public boolean isAreia_suja() {
        return areiaSuja;
    }

    // Metodo auxiliar antigo para compatibilidade com o codigo de console.
    public void setAreia_suja(boolean areiaSuja) {
        this.areiaSuja = areiaSuja;
    }

    // Sorteia uma sugestao de brincadeira para gato.
    @Override
    public String sugestoesBrincadeiras() {
        return switch (new Random().nextInt(4)) {
            case 0 -> "Laser";
            case 1 -> "Varinha de pesca";
            case 2 -> "Caca ao tesouro com petiscos";
            default -> "Construir um castelo de caixa";
        };
    }

    // Monta um texto simples com os dados principais do gato.
    @Override
    public String exibirAnimal() {
        return "Nome do seu gatinho e: " + nome + "\nDe idade: " + idade + "\nRaca: " + raca;
    }
}
