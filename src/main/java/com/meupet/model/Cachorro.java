package com.meupet.model;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Random;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "tb_cachorro")
public class Cachorro extends Animal {

    private LocalDate dataLastBanho;
    private LocalDate dataLastTosa;
    private LocalDate dataUltimoPasseio;

    private RacaCachorro raca;

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

    public Cachorro() {
        super();
    }

    public Cachorro(int id, String nome, int idade, Sexo sexo, float peso, boolean sujo, boolean castrado,
                    LocalDate dataLastBanho, LocalDate dataLastTosa, LocalDate dataUltimoPasseio,
                    RacaCachorro raca) {
        super(id, nome, idade, sexo, peso, sujo, castrado);
        this.dataLastBanho = dataLastBanho;
        this.dataLastTosa = dataLastTosa;
        this.dataUltimoPasseio = dataUltimoPasseio;
        this.raca = raca;
    }

    public Cachorro(int id, String nome, int idade, Sexo sexo, float peso, boolean sujo, boolean castrado,
                    String dataLastBanho, String dataLastTosa, String dataUltimoPasseio,
                    RacaCachorro raca) {
        this(id, nome, idade, sexo, peso, sujo, castrado,
                LocalDate.parse(dataLastBanho),
                LocalDate.parse(dataLastTosa),
                LocalDate.parse(dataUltimoPasseio),
                raca);
    }

    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId() {
        return super.getId();
    }

    @Override
    @Column(nullable = false)
    public String getNome() {
        return super.getNome();
    }

    @Override
    @Enumerated(EnumType.STRING)
    public Sexo getSexo() {
        return super.getSexo();
    }

    @Override
    public boolean isSujo() {
        return super.isSujo();
    }

    @Override
    public boolean isCastrado() {
        return super.isCastrado();
    }

    public void setSujo(boolean sujo) {
        this.sujo = sujo;
        if (!sujo) {
            super.limpar();
        }
    }

    public void setCastrado(boolean castrado) {
        this.castrado = castrado;
        if (castrado) {
            super.castrar();
        }
    }

    @Override
    public void limpar() {
        super.limpar();
    }

    @Override
    public void castrar() {
        super.castrar();
    }

    public LocalDate getDataLastBanho() {
        return dataLastBanho;
    }

    public void setDataLastBanho(LocalDate dataLastBanho) {
        this.dataLastBanho = dataLastBanho;
    }

    public LocalDate getDataLastTosa() {
        return dataLastTosa;
    }

    public void setDataLastTosa(LocalDate dataLastTosa) {
        this.dataLastTosa = dataLastTosa;
    }

    public LocalDate getDataUltimoPasseio() {
        return dataUltimoPasseio;
    }

    public void setDataUltimoPasseio(LocalDate dataUltimoPasseio) {
        this.dataUltimoPasseio = dataUltimoPasseio;
    }

    @Transient
    public LocalDate getData_last_banho() {
        return dataLastBanho;
    }

    @Transient
    public LocalDate getData_last_tosa() {
        return dataLastTosa;
    }

    @Transient
    public LocalDate getData_ultimo_passeio() {
        return dataUltimoPasseio;
    }

    @Enumerated(EnumType.STRING)
    public RacaCachorro getRaca() {
        return raca;
    }

    public void setRaca(RacaCachorro raca) {
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
        return "Nome do seu cachorrinho e: " + nome + "\nDe idade: " + idade + "\n" +
                "Raca: " + raca;
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
