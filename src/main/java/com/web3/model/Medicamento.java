package com.web3.model;

public class Medicamento {

    private int codigo;
    private String nome;
    private int dosagem;
    private String tipoDosagem;
    private String descricao;
    private String observacao;

    public Medicamento() {}

    public Medicamento(int codigo, String nome, int dosagem, String tipoDosagem, String descricao, String observacao) {
        this.codigo = codigo;
        this.nome = nome;
        this.dosagem = dosagem;
        this.tipoDosagem = tipoDosagem;
        this.descricao = descricao;
        this.observacao = observacao;
    }

    public int getCodigo() { return codigo; }
    public void setCodigo(int codigo) { this.codigo = codigo; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public int getDosagem() { return dosagem; }
    public void setDosagem(int dosagem) { this.dosagem = dosagem; }

    public String getTipoDosagem() { return tipoDosagem; }
    public void setTipoDosagem(String tipoDosagem) { this.tipoDosagem = tipoDosagem; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public String getObservacao() { return observacao; }
    public void setObservacao(String observacao) { this.observacao = observacao; }
}
