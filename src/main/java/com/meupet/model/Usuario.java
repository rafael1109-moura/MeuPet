package com.meupet.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tb_usuario")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Usuario implements Autenticavel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String email;
    
    @Column(nullable = false)
    private String senha;
    
    @Override
    public void login(String email, String senha) throws AutenticacaoException {
        if (this.email.equals(email) && this.senha.equals(senha)) {
            System.out.println("Login bem-sucedido! Bem-vindo, " + getNome());
        } else {
            throw new AutenticacaoException("Falha no login: Email ou senha incorretos para o usuário " + this.email);
        }
    }
    // @Override
    // public String toString() {
    //     return "Usuario{" +
    //             "ID:" + id +
    //             ", Nome:'" + nome + '\'' +
    //             ", Email:'" + email + '\'' +
    //             '}';
    // }
}
