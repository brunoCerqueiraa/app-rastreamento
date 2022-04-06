package com.gerentec.manutencao.modelo;

import java.io.Serializable;

public class TbEquipe implements Serializable {

    private Long idEquipe;
    private Long idFuncionario;
    private String nome;
    private String situacao;
    private String login;
    private String senha;
    private String imei;
    private String dataLogin;
    private Long idContrato;

    public String getDataLogin() {
        return dataLogin;
    }

    public void setDataLogin(String dataLogin) {
        this.dataLogin = dataLogin;
    }

    public Long getIdFuncionario() {
        return idFuncionario;
    }

    public void setIdFuncionario(Long idFuncionario) {
        this.idFuncionario = idFuncionario;
    }

    public Long getIdContrato() {
        return idContrato;
    }

    public void setIdContrato(Long idContrato) {
        this.idContrato = idContrato;
    }

    public Long getIdEquipe() {
        return idEquipe;
    }

    public void setIdEquipe(Long idEquipe) {
        this.idEquipe = idEquipe;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    @Override
    public String toString() {
        return "TbEquipe{" +
                "idEquipe=" + idEquipe +
                ", idFuncionario=" + idFuncionario +
                ", nome='" + nome + '\'' +
                ", situacao='" + situacao + '\'' +
                ", login='" + login + '\'' +
                ", senha='" + senha + '\'' +
                ", imei='" + imei + '\'' +
                ", idContrato=" + idContrato +
                '}';
    }
}
