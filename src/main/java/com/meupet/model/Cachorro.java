package com.meupet.model;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class Cachorro extends Animal {
    private String data_last_banho;
    private String data_last_tosa;
    private String data_ultimo_passeio;
    private RacaCachorro raca;

    public enum RacaCachorro {
    Bulldog, Golden_Retriever, Pastor_Alemão, Pinscher, Pug, Salsicha, Shih_Tzu, SRD;
}

    public void setRaca(RacaCachorro raca) {this.raca = raca;}
    public RacaCachorro getRaca() { return raca; }

    public Cachorro(int id, String nome, int idade, Sexo sexo, float peso, boolean sujo, boolean castrado,
                    String data_last_banho, String data_last_tosa, String data_ultimo_passeio, RacaCachorro raca) {
        super(id, nome, idade, sexo, peso, sujo, castrado);
        this.data_last_banho = data_last_banho;
        this.data_last_tosa = data_last_tosa;
        this.data_ultimo_passeio = data_ultimo_passeio;
        this.raca = raca;
    }

    @Override
    public List<Vacina> buscarVacinas(Map<String, List<Vacina>> vacinasPorPet) {
        return vacinasPorPet.get("Cachorro");
    }

    @Override
    public List<Vacina> buscarDoencas(Map<String, List<Vacina>> vacinasPorPet) {
        return vacinasPorPet.get("Cachorro");
    }

    @Override
    public String exibirAnimal() {
        return "Nome do seu cachorrinho é: " + nome + "\nDe idade: " + idade + "\n" +
           "Raça: " + raca.toString();
    }

    @Override
    public String sugestoesBrincadeiras() {
        return switch (new Random().nextInt(5)) {
            case 0 -> "Jogar bolinha";
            case 1 -> "Jogar frisbee";
            case 2 -> "Esconde-esconde";
            case 3 -> "Cabo de guerra";
            default -> "Ensinar um comando novo";
        };
    }

	public String getData_last_banho() { return data_last_banho; }

	public void setData_last_banho(String data_last_banho) {
		this.data_last_banho = data_last_banho;
	}

	public String getData_last_tosa() { return data_last_tosa; }

	public void setData_last_tosa(String data_last_tosa) {
		this.data_last_tosa = data_last_tosa;
	}

	public String getData_ultimo_passeio() { return data_ultimo_passeio; }

	public void setData_ultimo_passeio(String data_ultimo_passeio) {
		this.data_ultimo_passeio = data_ultimo_passeio;
	}
    
}