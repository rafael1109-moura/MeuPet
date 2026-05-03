package com.meupet.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PetSaude {
    private final Map<Integer, Doenca> tabelaDoencas;
    private final Map<String, List<Vacina>> vacinasPorPet;

    public PetSaude() {
        this.tabelaDoencas = new HashMap<>();
        this.vacinasPorPet = new HashMap<>();
        inicializarDados();
    }

    private void inicializarDados() {
        Doenca raiva = new Doenca(1, "Raiva", "Zoonose viral fatal", "Prevenção vacinal");
        Doenca cinomose = new Doenca(2, "Cinomose", "Vírus altamente contagioso", "Suporte intensivo");
        Doenca parvovirose = new Doenca(3, "Parvovirose", "Infecção gastrointestinal", "Internação e hidratação");
        Doenca rinotraqueite = new Doenca(4, "Rinotraqueíte", "Doença respiratória felina", "Antibióticos e suporte");

        tabelaDoencas.put(raiva.getId(), raiva);
        tabelaDoencas.put(cinomose.getId(), cinomose);
        tabelaDoencas.put(parvovirose.getId(), parvovirose);
        tabelaDoencas.put(rinotraqueite.getId(), rinotraqueite);

        List<Vacina> vacinasCao = new ArrayList<>();
        vacinasCao.add(new Vacina(101, "Antirrábica", "Dose anual", raiva));
        vacinasCao.add(new Vacina(102, "V8", "Proteção contra Cinomose e Parvo", cinomose));

        List<Vacina> vacinasGato = new ArrayList<>();
        vacinasGato.add(new Vacina(201, "Antirrábica Felina", "Dose anual", raiva));
        vacinasGato.add(new Vacina(202, "V3", "Proteção respiratória", rinotraqueite));

        vacinasPorPet.put("Cachorro", vacinasCao);
        vacinasPorPet.put("Gato", vacinasGato);
    }

    public List<Vacina> getVacinasPorTipo(String tipo) { return vacinasPorPet.getOrDefault(tipo, new ArrayList<>()); }

    public Doenca getDoencaPorId(int id) { return tabelaDoencas.get(id); }

    public Map<String, List<Vacina>> getMapaCompletoVacinas() { return vacinasPorPet; }
}