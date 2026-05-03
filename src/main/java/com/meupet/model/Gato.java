    package com.meupet.model;

    import java.util.List;
    import java.util.Map;
    import java.util.Random;

    public class Gato extends Animal {
        private boolean areia_suja;
        private RacaGato raca;
        
        public enum RacaGato {
            Maine_Coon, Persa, Siames, SRD;
        }

        public void setRaca(RacaGato raca){this.raca = raca;}
        public RacaGato getRaca(){return raca;}

        public Gato(int id, String nome, int idade, Sexo sexo, float peso, boolean sujo, boolean castrado, boolean areia_suja, RacaGato raca) {
            super(id, nome, idade, sexo, peso, sujo, castrado);
            this.areia_suja = areia_suja;
            this.raca = raca;
        }

        @Override
        public List<Vacina> buscarVacinas(Map<String, List<Vacina>> vacinasPorPet) {
            return vacinasPorPet.get("Gato");
        }

        @Override
        public List<Vacina> buscarDoencas(Map<String, List<Vacina>> doencasPorPet) {
            return doencasPorPet.get("Gato");
        }

        public void limparAreia() { this.areia_suja = false; }

        @Override
        public String sugestoesBrincadeiras() {
            return switch (new Random().nextInt(4)) {
                case 0 -> "Laser";
                case 1 -> "Varinha de pesca";
                case 2 -> "Caça ao Tesouro com Petiscos";
                default -> "Construir um castelo de caixa";
            };
        }

        @Override
        public String exibirAnimal() {
            return "Nome do seu gatinho é: " + nome + "\n De idade: " + idade + "\n" +
           "Raça: " + raca.toString();
        }

        public boolean isAreia_suja() { return areia_suja; }

        public void setAreia_suja(boolean areia_suja) { this.areia_suja = areia_suja; }
        
    }