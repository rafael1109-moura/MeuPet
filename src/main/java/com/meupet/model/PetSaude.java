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
    }

    public List<Vacina> getVacinasPorTipo(String tipo) { return vacinasPorPet.getOrDefault(tipo, new ArrayList<>()); }

    public Doenca getDoencaPorId(int id) { return tabelaDoencas.get(id); }

    public Map<String, List<Vacina>> getMapaCompletoVacinas() { return vacinasPorPet; }
}