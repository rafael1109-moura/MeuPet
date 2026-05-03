package com.meupet.model;

public class Vacina {
	private int id;
	private String nome;
	private String descricao;
	private Doenca doenca;
	
	public Vacina(int id, String nome, String descricao, Doenca doenca) {
		this.id = id;
		this.nome = nome;
		this.descricao = descricao;
		this.doenca = doenca;
	}

	public int getId() { return id; }
	public void setId(int id) { this.id = id; }
	public String getNome() { return nome; }
	public void setNome(String nome) { this.nome = nome; }
	public String getDescricao() { return descricao; }
	public void setDescricao(String descricao) { this.descricao = descricao; }
	public Doenca getDoenca() { return doenca; }
	public void setDoenca(Doenca doenca) { this.doenca = doenca; }
}