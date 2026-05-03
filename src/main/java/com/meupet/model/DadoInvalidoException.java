package com.meupet.model;

public class DadoInvalidoException extends Exception {
    public DadoInvalidoException(String mensagem) {
        super(mensagem);
    }
}