package com.meupet.model;

//resposavel por salvar no banco
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Random;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//classe vira uma tabela no banco de dados
@Entity
@Table(name = "tb_cachorro") //nome da tabela
@Getter
@Setter
@NoArgsConstructor // cria um construtor sem argumentos
@AllArgsConstructor // cria um construtor com todos os argumentos

public class Cachorro extends Animal {
    private LocalDate dataLastBanho;
    private LocalDate dataLastTosa;
    private LocalDate dataUltimoPasseio;

    @Enumerated(EnumType.STRING) //salva o nome da raça no banco de dados
    private RacaCachorro raca;
    public enum RacaCachorro {
        Bulldog,
        GoldenRetriever,
        PastorAlemao, 
        Pinscher,
        Pug,
        Salsicha,
        ShihTzu,
        SRD
    }

    //logica do sistema



    @Override
    public List<Vacina> buscarVacinas(Map<String, List<Vacina>> vacinasPorPet) {
        return vacinasPorPet.get("Cachorro");
    }

    //aqui  deveria ser doença, mas vou manter por enquanto
    @Override
    public List<Vacina> buscarDoencas(Map<String, List<Vacina>> vacinasPorPet) {
        return vacinasPorPet.get("Cachorro");
    }

    @Override
    public String exibirAnimal() {
        return "Nome do seu cachorrinho é: " + nome + "\nDe idade: " + idade + "\n" +
           "Raça: " + raca;
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
    
}