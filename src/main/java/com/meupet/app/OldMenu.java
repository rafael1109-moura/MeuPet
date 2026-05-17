package com.meupet.app;

import com.meupet.model.Usuario;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
//uso teste do annotations
@UnusedVersao(numero = "1.0", autor = "Lucas Lopes")
    
public class OldMenu {
    private Scanner scanner;

    public OldMenu(Scanner scanner) {
        this.scanner = scanner;
    }

    public void iniciar() {
        List<Usuario> usuarios = new ArrayList<>();

        while (true) {
            System.out.println("1- Listar usuarios");
            System.out.println("2- Adicionar usuario");
            System.out.println("3- Salvar usuarios");
            System.out.println("4- Sair");

            int op = scanner.nextInt();
            scanner.nextLine();

            switch (op) {
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
                    System.out.println("Opcao invalida, tente novamente.");
            }
        }
    }

    private void listar(List<Usuario> usuarios) {
        if (usuarios.isEmpty()) {
            System.out.println("Nenhum usuario cadastrado.");
        } else {
            usuarios.forEach(usuario -> System.out.println(usuario));
        }
    }

    private void adicionar(List<Usuario> usuarios) {
        System.out.println("Digite o nome do usuario:");
        String nome = scanner.nextLine();
        System.out.println("Digite o email do usuario:");
        String email = scanner.nextLine();
        System.out.println("Digite a senha do usuario:");
        String senha = scanner.nextLine();

        int id = usuarios.size() + 1;
        Usuario novoUsuario = new Usuario(id, nome, email, senha);
        usuarios.add(novoUsuario);
        System.out.println("Usuario adicionado com sucesso!");
    }

    private void salvar(List<Usuario> usuarios) {
        String jsonUser = usuariosParaJson(usuarios);

        try {
            FileWriter writer = new FileWriter("usuarios.json");
            writer.write(jsonUser);
            writer.close();
            System.out.println("Usuarios salvos com sucesso!");
        } catch (Exception e) {
            System.out.println("Erro ao salvar usuarios: " + e.getMessage());
        }
    }

    private String usuariosParaJson(List<Usuario> usuarios) {
        StringBuilder json = new StringBuilder("[\n");

        for (int i = 0; i < usuarios.size(); i++) {
            Usuario usuario = usuarios.get(i);
            json.append("  {")
                .append("\"id\": ").append(usuario.getId()).append(", ")
                .append("\"nome\": \"").append(escaparJson(usuario.getNome())).append("\", ")
                .append("\"email\": \"").append(escaparJson(usuario.getEmail())).append("\", ")
                .append("\"senha\": \"").append(escaparJson(usuario.getSenha())).append("\"")
                .append("}");

            if (i < usuarios.size() - 1) {
                json.append(",");
            }

            json.append("\n");
        }

        json.append("]");
        return json.toString();
    }

    private String escaparJson(String texto) {
        return texto
                .replace("\\", "\\\\")
                .replace("\"", "\\\"");
    }
}
