package com.meupet.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

// Entidade base para todos os animais do sistema.
@Entity
@Table(name = "tb_animal")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Animal {

    // Chave primaria gerada pelo banco.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Integer id;

    // Dados comuns herdados por Cachorro e Gato.
    @Column(nullable = false)
    protected String nome;

    @Column(nullable = false)
    protected int idade;

    @Enumerated(EnumType.STRING)
    protected Sexo sexo;

    protected float peso;
    protected boolean sujo;
    protected boolean castrado;

    // Lista usada pela regra de dominio, sem persistencia direta nesta classe.
    @Transient
    protected List<Doenca> doencas = new ArrayList<>();

    // Opcoes de sexo aceitas pelo sistema.
    public enum Sexo {
        MACHO, FEMEA
    }

    // Construtor usado pelo JPA.
    protected Animal() {
    }

    // Construtor com os dados comuns de qualquer animal.
    protected Animal(Integer id, String nome, int idade, Sexo sexo, float peso, boolean sujo, boolean castrado) {
        this.id = id;
        this.nome = validarNome(nome);
        this.idade = validarIdade(idade);
        this.sexo = sexo;
        this.peso = validarPeso(peso);
        this.sujo = sujo;
        this.castrado = castrado;
    }

    // Valida nome sem chamar metodo sobrescrevivel no construtor.
    private static String validarNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome e obrigatorio.");
        }
        return nome;
    }

    // Valida idade sem chamar metodo sobrescrevivel no construtor.
    private static int validarIdade(int idade) {
        if (idade < 0) {
            throw new IllegalArgumentException("Idade deve ser um valor positivo.");
        }
        return idade;
    }

    // Valida peso sem chamar metodo sobrescrevivel no construtor.
    private static float validarPeso(float peso) {
        if (peso < 0) {
            throw new IllegalArgumentException("Peso deve ser um valor positivo.");
        }
        return peso;
    }

    // Retorna todas as vacinas informadas no mapa.
    public List<Vacina> buscarVacinas(Map<String, List<Vacina>> vacinasPorPet) {
        return vacinasPorPet.values()
                .stream()
                .flatMap(List::stream)
                .toList();
    }

    // Retorna todas as doencas informadas no mapa.
    public List<Doenca> buscarDoencas(Map<String, List<Doenca>> doencasPorPet) {
        return doencasPorPet.values()
                .stream()
                .flatMap(List::stream)
                .toList();
    }

    // Atualiza os dados principais do animal.
    public void atualizarPerfil(String novoNome, int novaIdade) {
        setNome(novoNome);
        setIdade(novaIdade);
    }

    // Adiciona uma doenca ao historico em memoria.
    public void adicionarDoenca(Doenca doenca) {
        if (doenca == null) {
            throw new IllegalArgumentException("Doenca nao pode ser nula.");
        }
        doencas.add(doenca);
    }

    // Marca o animal como limpo.
    public void limpar() {
        this.sujo = false;
    }

    // Marca o animal como castrado.
    public void castrar() {
        this.castrado = true;
    }

    // Calcula racao com base no peso e na porcentagem informada.
    public float calcularRacao(float porcentagemAlimentacao) {
        if (porcentagemAlimentacao <= 0) {
            throw new IllegalArgumentException("Porcentagem de alimentacao deve ser positiva.");
        }
        return this.peso * porcentagemAlimentacao;
    }

    // Monta um texto simples com os dados principais.
    public String exibirAnimal() {
        return "Nome do seu pet: " + nome + "\nDe idade: " + idade;
    }

    protected void setId(Integer id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = validarNome(nome);
    }

    public void setIdade(int idade) {
        this.idade = validarIdade(idade);
    }

    public void setSexo(Sexo sexo) {
        this.sexo = sexo;
    }

    public void setPeso(float peso) {
        this.peso = validarPeso(peso);
    }

    public void setSujo(boolean sujo) {
        this.sujo = sujo;
    }

    public void setCastrado(boolean castrado) {
        this.castrado = castrado;
    }

    public Integer getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public int getIdade() {
        return idade;
    }

    public Sexo getSexo() {
        return sexo;
    }

    public float getPeso() {
        return peso;
    }

    public boolean isSujo() {
        return sujo;
    }

    public boolean isCastrado() {
        return castrado;
    }

    public List<Doenca> getDoencas() {
        return List.copyOf(doencas);
    }

    // Cada subclasse define suas brincadeiras.
    public abstract String sugestoesBrincadeiras();
}
