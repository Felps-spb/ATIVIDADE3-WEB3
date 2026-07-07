package com.web3.model;

public class Prontuario {

    private int codigo;
    private String descricao;
    private String observacao;
    private int consultaCodigo;

    public Prontuario() {}

    public int getCodigo() { return codigo; }
    public void setCodigo(int codigo) { this.codigo = codigo; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public String getObservacao() { return observacao; }
    public void setObservacao(String observacao) { this.observacao = observacao; }

    public int getConsultaCodigo() { return consultaCodigo; }
    public void setConsultaCodigo(int consultaCodigo) { this.consultaCodigo = consultaCodigo; }
}
