package com.gerentec.manutencao.modelo;

public class TbFuncionario {
    private Long idFuncionario;
    private Long idFuncionarioTp;
    private String nome;
    private String funcionarioTp;

    public String getFuncionarioTp() {
        return funcionarioTp;
    }

    public void setFuncionarioTp(String funcionarioTp) {
        this.funcionarioTp = funcionarioTp;
    }

    public Long getIdFuncionario() {
        return idFuncionario;
    }

    public void setIdFuncionario(Long idFuncionario) {
        this.idFuncionario = idFuncionario;
    }

    public Long getIdFuncionarioTp() {
        return idFuncionarioTp;
    }

    public void setIdFuncionarioTp(Long idFuncionarioTp) {
        this.idFuncionarioTp = idFuncionarioTp;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
