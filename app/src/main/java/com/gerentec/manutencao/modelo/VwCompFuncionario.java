package com.gerentec.manutencao.modelo;

public class VwCompFuncionario {
    //TbComp
    private Long comp_idComp;
    private Long comp_idFuncionario;
    private String comp_nome;
    private String comp_funcionarioTp;


    //TbFuncionario
    private Long func_idFuncionario;
    private Long func_idFuncionarioTp;
    private String func_nome;
    private String func_funcionarioTp;

    public Long getComp_idComp() {
        return comp_idComp;
    }

    public void setComp_idComp(Long comp_idComp) {
        this.comp_idComp = comp_idComp;
    }

    public Long getComp_idFuncionario() {
        return comp_idFuncionario;
    }

    public void setComp_idFuncionario(Long comp_idFuncionario) {
        this.comp_idFuncionario = comp_idFuncionario;
    }

    public String getComp_nome() {
        return comp_nome;
    }

    public void setComp_nome(String comp_nome) {
        this.comp_nome = comp_nome;
    }

    public String getComp_funcionarioTp() {
        return comp_funcionarioTp;
    }

    public void setComp_funcionarioTp(String comp_funcionarioTp) {
        this.comp_funcionarioTp = comp_funcionarioTp;
    }

    public Long getFunc_idFuncionario() {
        return func_idFuncionario;
    }

    public void setFunc_idFuncionario(Long func_idFuncionario) {
        this.func_idFuncionario = func_idFuncionario;
    }

    public Long getFunc_idFuncionarioTp() {
        return func_idFuncionarioTp;
    }

    public void setFunc_idFuncionarioTp(Long func_idFuncionarioTp) {
        this.func_idFuncionarioTp = func_idFuncionarioTp;
    }

    public String getFunc_nome() {
        return func_nome;
    }

    public void setFunc_nome(String func_nome) {
        this.func_nome = func_nome;
    }

    public String getFunc_funcionarioTp() {
        return func_funcionarioTp;
    }

    public void setFunc_funcionarioTp(String func_funcionarioTp) {
        this.func_funcionarioTp = func_funcionarioTp;
    }
}
