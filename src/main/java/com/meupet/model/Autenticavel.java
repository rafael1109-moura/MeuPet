package com.meupet.model;

public interface Autenticavel {
    void login(String email, String senha) throws AutenticacaoException;
}