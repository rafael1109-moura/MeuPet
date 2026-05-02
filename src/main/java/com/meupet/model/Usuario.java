package com.meupet.model;

public class Usuario implements Autenticavel {
    private int id;
    private String nome;
    private String email;
    private String senha;

    public Usuario(int id, String nome, String email, String senha) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

    @Override
    public void login(String email, String senha) throws AutenticacaoException {
        if (this.email.equals(email) && this.senha.equals(senha)) {
            System.out.println("Login bem-sucedido! Bem-vindo, " + getNome());
        } else {
            throw new AutenticacaoException("Falha no login: Email ou senha incorretos para o usuário " + this.email);
        }
    }
    @Override
    public String toString() {
        return "Usuario{" +
                "ID:" + id +
                ", Nome:'" + nome + '\'' +
                ", Email:'" + email + '\'' +
                '}';
    }
    
}
