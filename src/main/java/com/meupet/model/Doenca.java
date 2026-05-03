package com.meupet.model;

public class Doenca {
	private int id;
	private String nome;
	private String descricao;
	private String tratamento;
	
	public Doenca(int id, String nome, String descricao, String tratamento) {
		this.id = id;
		this.nome = nome;
		this.descricao = descricao;
		this.tratamento = tratamento;
	}

	public int getId() { return id; }
	public void setId(int id) { this.id = id; }
	public String getNome() { return nome; }
	public void setNome(String nome) { this.nome = nome; }
	public String getDescricao() { return descricao; }
	public void setDescricao(String descricao) { this.descricao = descricao; }
	public String getTratamento() { return tratamento; }
	public void setTratamento(String tratamento) { this.tratamento = tratamento; }
}