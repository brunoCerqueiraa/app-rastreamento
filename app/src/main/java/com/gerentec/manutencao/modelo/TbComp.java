package com.gerentec.manutencao.modelo;

public class TbComp {
    private Long idFuncionario;
    private String nome;
    private String funcionarioTp;

    public Long getIdFuncionario() {
        return idFuncionario;
    }

    public void setIdFuncionario(Long idFuncionario) {
        this.idFuncionario = idFuncionario;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getFuncionarioTp() {
        return funcionarioTp;
    }

    public void setFuncionarioTp(String funcionarioTp) {
        this.funcionarioTp = funcionarioTp;
    }
}
