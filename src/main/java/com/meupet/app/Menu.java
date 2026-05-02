package com.meupet.app;

import java.util.Scanner;
import java.util.List;
import com.meupet.model.Usuario;
import java.util.ArrayList;
import com.google.gson.Gson;
import java.io.FileWriter;



public class Menu {
    // classe para ler dados, system é teclado
    private Scanner scanner = new Scanner(System.in);

    public void iniciar(){
        List<Usuario> usuarios = new ArrayList<>();
        while (true) { 
         System.out.println("Bem-vindo ao MeuPet!");
         System.out.println("1- listar usuários");
         System.out.println("2- Adicionar usuário");
         System.out.println("3- Salvar usuários");
         System.out.println("4- Sair");
         
         int op = scanner.nextInt(); // recebe a opção que a pessoa digitou
         scanner.nextLine(); // Limpar o buffer do scanner

         switch (op){
            case 1:
                listar(usuarios);
                break;
            case 2:
                adicionar(usuarios);
                break;
            case 3:
                salvar(usuarios);
                break;
            case 4:
                return;
             default:
                System.out.println("Opção inválida, tente novamente.");
                
         }
        }
    }
    private  void listar (List<Usuario> usuarios){
        if (usuarios.isEmpty()){
            System.out.println("Nenhum usuário cadastrado.");
        } else {
            usuarios.forEach(usuario -> System.out.println(usuario));
        }
    }

    private void adicionar(List<Usuario> usuarios){
        System.out.println("Digite o nome do usuário:");
        String nome = scanner.nextLine();
        System.out.println("Digite o email do usuário:");
        String email = scanner.nextLine();
        System.out.println("Digite a senha do usuário:");
        String senha = scanner.nextLine();

        int id = usuarios.size() + 1; // Gerar ID baseado no tamanho da lista
        Usuario novoUsuario = new Usuario(id, nome, email, senha);
        usuarios.add(novoUsuario);
        System.out.println("Usuário adicionado com sucesso!");
    }

    private void salvar(List<Usuario> usuarios){
        String jsonUser = new Gson().toJson(usuarios);
        try {
            FileWriter writer = new FileWriter("usuarios.json");
            writer.write(jsonUser);
            writer.close();
            System.out.println("Usuários salvos com sucesso!");
        } catch (Exception e) {
            System.out.println("Erro ao salvar usuários: " + e.getMessage());
        }
    }
    
    
}
