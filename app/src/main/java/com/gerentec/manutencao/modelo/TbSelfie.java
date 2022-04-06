package com.gerentec.manutencao.modelo;

public class TbSelfie {
    private Long idSelfie;
    private Long idEquipe;
    private String finalizado;

    public Long getIdSelfie() {
        return idSelfie;
    }

    public void setIdSelfie(Long idSelfie) {
        this.idSelfie = idSelfie;
    }

    public Long getIdEquipe() {
        return idEquipe;
    }

    public void setIdEquipe(Long idEquipe) {
        this.idEquipe = idEquipe;
    }

    public String getFinalizado() {
        return finalizado;
    }

    public void setFinalizado(String finalizado) {
        this.finalizado = finalizado;
    }
}
