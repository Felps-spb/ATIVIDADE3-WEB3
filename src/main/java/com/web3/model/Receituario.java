package com.web3.model;

public class Receituario {

    private int codigo;
    private String observacao;
    private int prontuarioCodigo;

    public Receituario() {}

    public int getCodigo() { return codigo; }
    public void setCodigo(int codigo) { this.codigo = codigo; }

    public String getObservacao() { return observacao; }
    public void setObservacao(String observacao) { this.observacao = observacao; }

    public int getProntuarioCodigo() { return prontuarioCodigo; }
    public void setProntuarioCodigo(int prontuarioCodigo) { this.prontuarioCodigo = prontuarioCodigo; }
}
