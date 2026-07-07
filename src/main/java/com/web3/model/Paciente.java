package com.web3.model;

public class Paciente {

    private String cpf;
    private String nome;
    private String endereco;
    private String contato;
    private String planoSaude;

    public Paciente() {}

    public Paciente(String cpf, String nome, String endereco, String contato, String planoSaude) {
        this.cpf = cpf;
        this.nome = nome;
        this.endereco = endereco;
        this.contato = contato;
        this.planoSaude = planoSaude;
    }

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }

    public String getContato() { return contato; }
    public void setContato(String contato) { this.contato = contato; }

    public String getPlanoSaude() { return planoSaude; }
    public void setPlanoSaude(String planoSaude) { this.planoSaude = planoSaude; }
}
