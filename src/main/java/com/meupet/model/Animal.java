package com.meupet.model;
//teste
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;



public abstract class Animal {
    protected int id;
    protected String nome;
    protected int idade;
    protected Sexo sexo;
    protected float peso;
    protected boolean sujo;
    protected boolean castrado;
    protected ArrayList<Doenca> doencas;
    

    public enum Sexo {
        MACHO, FEMEA;
    }
    public Animal() {}
    public Animal(int id, String nome, int idade, Sexo sexo, float peso, boolean sujo, boolean castrado) {
        this.id = id;
        this.nome = nome;
        this.idade = idade;
        this.sexo = sexo;
        this.peso = peso;
        this.sujo = sujo;
        this.castrado = castrado;
        this.doencas = new ArrayList<>();
    }

    public List<Vacina> buscarVacinas(Map<String, List<Vacina>> vacinasPorPet) {
        return vacinasPorPet.values().stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    public List<Vacina> buscarDoencas(Map<String, List<Vacina>> doencasPorPet) {
        return doencasPorPet.values().stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }
    

    public int getId() { return id; }
    public String getNome() { return nome; }
    public int getIdade() { return idade; }
    public Sexo getSexo() { return sexo; }
    public float getPeso() { return peso; }
    public boolean isSujo() { return sujo; }
    public boolean isCastrado() { return castrado; }
    public ArrayList<Doenca> getDoencas() { return doencas; }

    public void atualizarPerfil(String novoNome, int novaIdade) throws DadoInvalidoException {
        this.setNome(novoNome);
        setIdade(novaIdade);
    }
    
    public void setIdade(int idade) throws DadoInvalidoException {
        if (idade < 0) {
            throw new DadoInvalidoException("Idade inválida: " + idade + ". A idade deve ser um valor positivo.");
        }
        this.idade = idade;
    }
    public void setPeso(float peso) throws DadoInvalidoException {
    	if (peso < 0) {
            throw new DadoInvalidoException("Peso inválido: " + peso + ". O peso deve ser um valor positivo.");
        }
    	this.peso = peso; 
    }

    
    public void adicionarDoenca(Doenca doenca) {
        this.doencas.add(doenca);
    }

    public void setId(int id) { this.id = id; }
    public void setNome(String nome) { this.nome = nome; }
    public void setSexo(Sexo sexo) { this.sexo = sexo; }
    public void limpar() { this.sujo = false; }
    public void castrar() { this.castrado = true; }

    public float calcularRacao(float porcentagem_alimentacao) throws DadoInvalidoException {
        return this.peso * porcentagem_alimentacao;
    }

    public String exibirAnimal() {
        return "Nome do seu pet: " + nome + "\n De idade: " + idade;
    }

    public abstract String sugestoesBrincadeiras();
}