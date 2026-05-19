package com.meupet.model;

import java.time.LocalDate;
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

// Indica que esta classe sera uma tabela no banco de dados.
@Entity
// Define o nome da tabela usada para salvar cachorros.
@Table(name = "tb_cachorro")
@Getter
@Setter
public class Cachorro extends Animal {

    // Datas importantes para acompanhar os cuidados do cachorro.
    private LocalDate dataLastBanho;
    private LocalDate dataLastTosa;
    private LocalDate dataUltimoPasseio;

    // Raca do cachorro, salva pelo nome do enum.
    @Enumerated(EnumType.STRING)
    private RacaCachorro raca;

    // Opcoes de raca aceitas pelo sistema.
    public enum RacaCachorro {
        Bulldog,
        GoldenRetriever,
        Golden_Retriever,
        PastorAlemao,
        Pinscher,
        Pug,
        Salsicha,
        ShihTzu,
        SRD
    }

    // Construtor vazio exigido pelo JPA.
    public Cachorro() {
        super();
    }

    // Construtor usado quando as datas ja chegam como LocalDate.
    public Cachorro(Integer id, String nome, int idade, Sexo sexo, float peso, boolean sujo, boolean castrado,
                    LocalDate dataLastBanho, LocalDate dataLastTosa, LocalDate dataUltimoPasseio,
                    RacaCachorro raca) {
        super(id, nome, idade, sexo, peso, sujo, castrado);
        this.dataLastBanho = dataLastBanho;
        this.dataLastTosa = dataLastTosa;
        this.dataUltimoPasseio = dataUltimoPasseio;
        this.raca = raca;
    }

    // Construtor auxiliar para criar cachorro recebendo datas em formato de texto.
    public Cachorro(Integer id, String nome, int idade, Sexo sexo, float peso, boolean sujo, boolean castrado,
                    String dataLastBanho, String dataLastTosa, String dataUltimoPasseio,
                    RacaCachorro raca) {
        this(id, nome, idade, sexo, peso, sujo, castrado,
                LocalDate.parse(dataLastBanho),
                LocalDate.parse(dataLastTosa),
                LocalDate.parse(dataUltimoPasseio),
                raca);
    }

    // Atualiza o estado de sujeira e aplica a regra de limpeza quando necessario.
    @Override
    public void setSujo(boolean sujo) {
        this.sujo = sujo;
        if (!sujo) {
            super.limpar();
        }
    }

    // Atualiza o estado de castracao e aplica a regra de castrar quando necessario.
    @Override
    public void setCastrado(boolean castrado) {
        this.castrado = castrado;
        if (castrado) {
            super.castrar();
        }
    }

    // Metodo auxiliar antigo. O @Transient impede que vire coluna no banco.
    @Transient
    public LocalDate getData_last_banho() {
        return dataLastBanho;
    }

    // Metodo auxiliar antigo. O @Transient impede que vire coluna no banco.
    @Transient
    public LocalDate getData_last_tosa() {
        return dataLastTosa;
    }

    // Metodo auxiliar antigo. O @Transient impede que vire coluna no banco.
    @Transient
    public LocalDate getData_ultimo_passeio() {
        return dataUltimoPasseio;
    }

    // Retorna as vacinas recomendadas para cachorros.
    @Override
    public List<Vacina> buscarVacinas(Map<String, List<Vacina>> vacinasPorPet) {
        return vacinasPorPet.get("Cachorro");
    }

    // Retorna as doencas cadastradas para cachorros.
    @Override
    public List<Doenca> buscarDoencas(Map<String, List<Doenca>> doencasPorPet) {
        return doencasPorPet.get("Cachorro");
    }

    // Monta um texto simples com os dados principais do cachorro.
    @Override
    public String exibirAnimal() {
        return "Nome do seu cachorrinho e: " + nome + "\nDe idade: " + idade + "\n" +
                "Raca: " + raca;
    }

    // Sorteia uma sugestao de brincadeira para cachorro.
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
