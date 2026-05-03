package com.meupet.app;

import com.meupet.model.Animal;
import com.meupet.model.AutenticacaoException;
import com.meupet.model.Cachorro;
import com.meupet.model.Cachorro.RacaCachorro;
import com.meupet.model.DadoInvalidoException;
import com.meupet.model.Gato;
import com.meupet.model.Gato.RacaGato;
import com.meupet.model.PetSaude;
import com.meupet.model.Usuario;
import com.meupet.model.Vacina;
import java.util.List;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        PetSaude saude = new PetSaude();

        //==================================================================================
        // Menu para gerenciamento de usuários
        Menu menu = new Menu(scanner); // cria o menu
        menu.iniciar();         // executa o menu
        

        //=================================================""=================================


        //pega nome do cachorro
        System.out.print("Digite o nome do cachorro: ");
        String nomeCachorro = scanner.nextLine();
        
        //nome do gato
        System.out.print("Digite o nome do gato: ");
        String nomeGato = scanner.nextLine();
        
        System.out.println();
        
        Cachorro cachorro = new Cachorro(1,
            nomeCachorro,
            3,
            Animal.Sexo.MACHO,
            10.5f,
            false,
            false,
            "2024-06-01",
            "2024-06-02", 
            "2024-06-03",
            RacaCachorro.Golden_Retriever 
        );
        
        List<Vacina> vacinasCachorro = cachorro.buscarVacinas(saude.getMapaCompletoVacinas());
        
        System.out.println("Histórico de vacinas recomendadas para " + cachorro.getNome() + ":");
        
        for(Vacina v : vacinasCachorro) {
            System.out.println("- " + v.getNome() + ", previne " + v.getDoenca().getNome());
        }
        
        System.out.println();
        
        Gato gato = new Gato(2,
            nomeGato,
            2,
            Animal.Sexo.FEMEA,
            4.0f,
            false,
            true,
            true,
            RacaGato.Siames
        );
        
        List<Vacina> vacinasGato = gato.buscarVacinas(saude.getMapaCompletoVacinas());
        
        System.out.println("Histórico de vacinas recomendadas para " + gato.getNome() + ":");
        for(Vacina v : vacinasGato) {
            System.out.println("- " + v.getNome() + ", previne " + v.getDoenca().getNome());
        }
        
        System.out.println();
        
        System.out.println("Data do último banho: " + cachorro.getData_last_banho());
        System.out.println("Data da última tosa: " + cachorro.getData_last_tosa());
        System.out.println("Data do último passeio: " + cachorro.getData_ultimo_passeio());
        
        
        System.out.println();
        
        System.out.println("A areia está suja? " + gato.isAreia_suja());

        gato.limparAreia();

        System.out.println("A areia está suja? " + gato.isAreia_suja());

        System.out.println("Sugestão de brincadeira: " + cachorro.sugestoesBrincadeiras());
        System.out.println("Sugestão de brincadeira: " + gato.sugestoesBrincadeiras());
        System.out.println(cachorro.exibirAnimal());
        System.out.println(gato.exibirAnimal());
        
        System.out.println("\n\n\n");
        
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
        scanner.close();
    }
}
