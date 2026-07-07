package com.web3.model;

public class Consulta {

    private int codigo;
    private String dataHora;
    private String dataHoraVolta;
    private String observacao;
    private String pacienteCpf;
    private String medicoCrm;

    private String pacienteNome;
    private String medicoNome;

    public Consulta() {}

    public int getCodigo() { return codigo; }
    public void setCodigo(int codigo) { this.codigo = codigo; }

    public String getDataHora() { return dataHora; }
    public void setDataHora(String dataHora) { this.dataHora = dataHora; }

    public String getDataHoraVolta() { return dataHoraVolta; }
    public void setDataHoraVolta(String dataHoraVolta) { this.dataHoraVolta = dataHoraVolta; }

    public String getObservacao() { return observacao; }
    public void setObservacao(String observacao) { this.observacao = observacao; }

    public String getPacienteCpf() { return pacienteCpf; }
    public void setPacienteCpf(String pacienteCpf) { this.pacienteCpf = pacienteCpf; }

    public String getMedicoCrm() { return medicoCrm; }
    public void setMedicoCrm(String medicoCrm) { this.medicoCrm = medicoCrm; }

    public String getPacienteNome() { return pacienteNome; }
    public void setPacienteNome(String pacienteNome) { this.pacienteNome = pacienteNome; }

    public String getMedicoNome() { return medicoNome; }
    public void setMedicoNome(String medicoNome) { this.medicoNome = medicoNome; }
}
