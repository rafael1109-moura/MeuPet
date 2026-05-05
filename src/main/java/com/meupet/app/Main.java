package com.meupet.app;

import com.meupet.model.*;
import java.util.List;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) {

        // Lendo a anotação teste da classe Menu usando reflection
        exibirBannerSistema();//codigo no fina do arq
        
        Scanner scanner = new Scanner(System.in);

        //==================================================================================
        // Menu para gerenciamento de usuários
        Menu menu = new Menu(scanner); // cria o menu
        menu.iniciar();         // executa o menu
        
        desenharLinha();

        //=================================================""=================================


        //pega nome do cachorro
        System.out.print("Digite o nome do seu cachorro: ");
        String nomeCachorro = scanner.nextLine();
        
        //nome do gato
        System.out.print("Digite o nome do seu gato: ");
        String nomeGato = scanner.nextLine();
        
        PetSaude saude = new PetSaude();

        Cachorro cachorro = criarCachorroExemplo(nomeCachorro);
        exibirFichaAnimal(cachorro, saude);
            
        System.out.println("Data do último banho: " + cachorro.getData_last_banho());
        System.out.println("Data da última tosa: " + cachorro.getData_last_tosa());
        System.out.println("Data do último passeio: " + cachorro.getData_ultimo_passeio());
        System.out.println("Sugestão de brincadeira: " + cachorro.sugestoesBrincadeiras());

        Gato gato = criarGatoExemplo(nomeGato);
        exibirFichaAnimal(gato, saude);
        
        System.out.println("Sugestão de brincadeira: " + gato.sugestoesBrincadeiras());
        System.out.println("A areia está suja? " + gato.isAreia_suja());
        gato.limparAreia();
        System.out.println("A areia está suja? " + gato.isAreia_suja());

        /*
        //TESTES DE EXCEÇÃO PARA INCREMENTO
        //ainda não incrementados e oncorporados realmente ao codigo.

        try {
            cachorro.setIdade(-5); 
        } catch (DadoInvalidoException e) {
            System.out.println("Erro: " + e.getMessage());
        }

        try {
            cachorro.atualizarPerfil("Rex Segundo", 5);
            System.out.println("Perfil atualizado com sucesso!");
        } catch (DadoInvalidoException e) {
            System.out.println("Erro:  " + e.getMessage());
        }

        System.out.println(cachorro.exibirAnimal());

        System.out.println("\n");
        Usuario user = new Usuario(1, "Usuario", "user@email.com", "senha123");

        try {
            user.login("user@email.com", "12345");
        } catch (AutenticacaoException e) {
            System.out.println("Erro: "+ e.getMessage());
        }

        try {
            user.login("user@email.com", "senha123");
        } catch (AutenticacaoException e) {
            System.out.println(e.getMessage());
        }
        */

        scanner.close();

    }

    private static void desenharLinha() {
        System.out.println("============================================== == ========");
    }

    private static void exibirBannerSistema() {
        Class<?> classeMenu = Menu.class;
        if (classeMenu.isAnnotationPresent(Versao.class)) {
            Versao anotação = classeMenu.getAnnotation(Versao.class);
            desenharLinha();
            System.out.println("  Bem-vindo ao MeuPet!");
            System.out.println("  Versão de testes: " + anotação.numero());
            System.out.println("  Autor: " + anotação.autor());
            desenharLinha();
            System.out.println();
        }
    }

    private static Cachorro criarCachorroExemplo(String nome) {
        return new Cachorro(1, nome, 3, Animal.Sexo.MACHO, 10.5f, false, false, 
                           "2024-06-01", "2024-06-02", "2024-06-03", 
                           Cachorro.RacaCachorro.Golden_Retriever);
    }

    private static Gato criarGatoExemplo(String nome) {
        return new Gato(2, nome, 2, Animal.Sexo.FEMEA, 4.0f, false, true, true, 
                       Gato.RacaGato.Siames);
    }

    private static void exibirFichaAnimal(Animal animal, PetSaude saude) {
        System.out.println("\n>>> FICHA DO ANIMAL: " + animal.getNome().toUpperCase() + " <<<");
        System.out.println(animal.exibirAnimal());
        
        System.out.println("Vacinas Recomendadas:");
        List<Vacina> vacinas = animal.buscarVacinas(saude.getMapaCompletoVacinas());
        for(Vacina v : vacinas) {
            System.out.println("  [ ] " + v.getNome() + " (Previne: " + v.getDoenca().getNome() + ")");
        }
        desenharLinha();
    }
}


