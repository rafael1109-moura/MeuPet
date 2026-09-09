package com.meupet.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

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
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (this.email.equals(email) && encoder.matches(senha, this.senha)) {
            System.out.println("Login bem-sucedido! Bem-vindo, " + getNome());
        } else {
            throw new AutenticacaoException("Falha no login: Email ou senha incorretos para o usuario " + this.email);
        }
    }
}
